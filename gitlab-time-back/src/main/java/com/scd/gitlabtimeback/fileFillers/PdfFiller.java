package com.scd.gitlabtimeback.fileFillers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.enums.GroupingByField;

import org.decimal4j.util.DoubleRounder;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class PdfFiller {
    private final float PAGE_TOP_GAP = 50;
    private final float PAGE_BOTTOM_GAP = 50;
    private final float PAGE_LEFT_GAP = 50;
    private final float PAGE_RIGHT_GAP = 50;
    private final float PAGE_WIDTH = new PDPage().getMediaBox().getWidth();
    private final float PAGE_HEIGHT = new PDPage().getMediaBox().getHeight();
    private final float HOURS_COL_WIDTH = 75;
    private final Color BORDERS_COLOR = Color.WHITE;

    private final int HEADER_FONT_SIZE = 14;
    private final Color HEADER_TEXT_COLOR = Color.WHITE;
    private final Color HEADER_BACKGROUND_COLOR = new Color(0x001C95);

    private final int FONT_SIZE = 12;
    private final Color TEXT_COLOR = Color.BLACK;
    private final Color[] BACKGROUND_COLORS = { new Color(0xBACEE6), new Color(0xDAE6F2) };

    private final int FOOTER_FONT_SIZE = 12;
    private final Color FOOTER_TEXT_COLOR = Color.WHITE;
    private final Color FOOTER_BACKGROUND_COLOR = new Color(0x001C95);
    private final int TOTAL_TIME_NUMBER_FONT_SIZE = 14;
    private final Color TOTAL_TIME_NUMBER_CELL_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private final Color TOTAL_TIME_NUMBER_CELL_TEXT_COLOR = Color.BLACK;

    @Value("classpath:fonts/NimbusRomNo9L-Reg.ttf")
    private Resource fontResource;

    public byte[] fillFile(List<GroupingByField> grouping, List<GroupingReportDto> data){
        try (PDDocument document = new PDDocument()) {
            TableBuilder tableBuilder = Table.builder();
            presetTable(tableBuilder, document);
            addCols(tableBuilder, grouping);

            addHeader(tableBuilder, grouping);
            addBody(tableBuilder, data, 0, grouping.size(), new AtomicInteger(0));
            addFooter(tableBuilder, data, grouping.size());

            drawTableInDocument(tableBuilder, document);
            return getDocumentBytes(document);
        } catch (IOException e) {
            throw new RuntimeException("Could not create or fill pdf document", e);
        }
    }


    private TextCell createCell(String text){
        return TextCell.builder().text(text.replace('\t', ' ')).build();
    }

    private void drawBodyRow(TableBuilder tableBuilder, Color backgroundColor, String text, int textLevel, String value, int valueLevel){
        RowBuilder rowBuilder = Row.builder();
        for (int i = 0; i < textLevel; i++) {
            rowBuilder.add(createCell(""));
        }
        rowBuilder.add(createCell(text));
        for (int i = textLevel + 1; i < valueLevel; i++) {
            rowBuilder.add(createCell(""));
        }
        rowBuilder.add(createCell(value));
        tableBuilder.addRow(rowBuilder
                        .backgroundColor(backgroundColor)
                .build());
    }

    private void presetTable(TableBuilder tableBuilder, PDDocument document) throws IOException {
        tableBuilder.fontSize(FONT_SIZE)
                .font(PDType0Font.load(document, fontResource.getInputStream()))
                .textColor(TEXT_COLOR)
                .borderColor(BORDERS_COLOR)
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .borderWidth(1);
    }

    private void addCols(TableBuilder tableBuilder, List<GroupingByField> grouping){
        float innerPageWidth = PAGE_WIDTH - PAGE_LEFT_GAP - PAGE_RIGHT_GAP;
        for (int i = 0; i < grouping.size(); i++) {
            tableBuilder.addColumnOfWidth((innerPageWidth - HOURS_COL_WIDTH) / grouping.size());
        }
        tableBuilder.addColumnOfWidth(HOURS_COL_WIDTH);
    }

    private void addHeader(TableBuilder tableBuilder, List<GroupingByField> grouping){
        RowBuilder rowBuilder = Row.builder();
        grouping.forEach(field -> rowBuilder.add(createCell(field.toString())));
        rowBuilder.add(createCell("HOURS"));
        tableBuilder.addRow(rowBuilder
                        .backgroundColor(HEADER_BACKGROUND_COLOR)
                        .textColor(HEADER_TEXT_COLOR)
                        .fontSize(HEADER_FONT_SIZE)
                .build());
    }

    private void addBody(TableBuilder tableBuilder, List<GroupingReportDto> data, int level, int groups, AtomicInteger backgroundColorIndex){
        data.forEach(d -> {
            drawBodyRow(tableBuilder,
                    BACKGROUND_COLORS[backgroundColorIndex.getAndUpdate(v -> (v + 1) % BACKGROUND_COLORS.length)],
                    d.getNames().stream().map(cell->cell.getText()).collect(Collectors.joining(" ")), level, Double.toString(DoubleRounder.round(d.getTime(), 2)), groups);
            addBody(tableBuilder, d.getSubGroup(), level + 1, groups, backgroundColorIndex);
        });
    }

    private void addFooter(TableBuilder tableBuilder, List<GroupingReportDto> data, int groups){
        RowBuilder rowBuilder = Row.builder();
        rowBuilder.add(createCell("Total time:"));
        for (int i = 1; i < groups; i++) {
            rowBuilder.add(createCell(""));
        }

        String totalTime = Double.toString(DoubleRounder.round(data.stream().mapToDouble(GroupingReportDto::getTime).sum(),2));
        rowBuilder.add(TextCell.builder()
                .text(totalTime)
                .backgroundColor(TOTAL_TIME_NUMBER_CELL_BACKGROUND_COLOR)
                .textColor(TOTAL_TIME_NUMBER_CELL_TEXT_COLOR)
                .fontSize(TOTAL_TIME_NUMBER_FONT_SIZE)
                .build());

        tableBuilder.addRow(rowBuilder
                .backgroundColor(FOOTER_BACKGROUND_COLOR)
                .textColor(FOOTER_TEXT_COLOR)
                .fontSize(FOOTER_FONT_SIZE)
                .build());
    }

    private void drawTableInDocument(TableBuilder tableBuilder, PDDocument document) throws IOException {
        TableDrawer.builder()
                .table(tableBuilder.build())
                .startX(PAGE_LEFT_GAP)
                .startY(PAGE_HEIGHT - PAGE_TOP_GAP)
                .endY(PAGE_BOTTOM_GAP)
                .build()
                .draw(() -> document, PDPage::new, 50);
    }

    private byte[] getDocumentBytes(PDDocument document) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        return outputStream.toByteArray();
    }
}
