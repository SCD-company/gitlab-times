package com.scd.gitlabtimeback.fileFillers;

import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.enums.GroupingByField;

import java.nio.charset.StandardCharsets;
import org.decimal4j.util.DoubleRounder;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvFiller {
    public byte[] fillFile(List<GroupingByField> grouping, List<GroupingReportDto> data) {
        var levels = countLevels(grouping);
    
        return (fillHeader(grouping) + fillBody(grouping, data, 0,levels) + fillFooter(data,levels ))
                .replace("\t", " ")
                .getBytes(StandardCharsets.UTF_8);
    }

    private int countLevels(List<GroupingByField> grouping) {
        return grouping.stream().reduce(0,(subtotal,element)->{
            return subtotal + element.getHeaders().size();
        },Integer::sum);
    }

    private String fillHeader(List<GroupingByField> grouping) {
        StringBuilder str = new StringBuilder();

        grouping.forEach(resultEntry -> {
            resultEntry.getHeaders().forEach(header->{
                str.append(header.concat(", "));
            });
        });
                

        str.append("HOURS\n");

        return str.toString();
    }

    private String fillBody(List<GroupingByField> grouping, List<GroupingReportDto> data, Integer level, int levels) {

        if (data.isEmpty()) {
            return "";
        }

        var str = new StringBuilder();

        data.forEach(row -> {
            str.append(", ".repeat(Math.max(0, level)));
            str.append(row.getNames().stream().map(name->{
                switch(name.getCellType()) {
                    case TEXT:
                        return name.getText().replace(",", " ");
                    case LINK:
                        var href = ((GroupingReportDto.LinkCell)name).getHref();
                        return href.contains(",")?name.getText():href;
                    default: 
                        throw new RuntimeException();
                }
            }
            ).collect(Collectors.joining(",")).concat(", "));
            str.append(", ".repeat(Math.max(0, levels - row.getNames().size()-level)));
            if(row.getSubGroup().isEmpty()) {
                str.append(Double.toString(DoubleRounder.round(row.getTime(),4)));
            }
            str.append("\n");
            str.append(fillBody(grouping, row.getSubGroup(), level + row.getNames().size(),levels));
        });

        return str.toString();
    }

    private String fillFooter(List<GroupingReportDto> data, Integer levels) {

        var str = new StringBuilder("Total time:");
        str.append(", ".repeat(Math.max(0, levels)));

        String totalTime = Double.toString(DoubleRounder.round(data.stream().mapToDouble(GroupingReportDto::getTime).sum(),2));
        str.append(totalTime);

        return str.toString();
    }
}
