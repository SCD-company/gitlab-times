package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.scd.gitlabtimeback.enums.GroupingByField;

@AllArgsConstructor
@Setter
@Getter
public class GroupingReportDto {

    public static enum CellType {
        TEXT, LINK
    }

    @RequiredArgsConstructor
    @Getter
    public static abstract class Cell {
        private final CellType cellType;
        public abstract String getText();
    }

    @Getter
    public static class TextCell extends Cell {

        private final String text;

        public TextCell( String text) {
            super(CellType.TEXT);
            this.text = text;
        }

    }


    @Getter
    public static class LinkCell extends Cell {
        private final String text;
        private final String href;

        public LinkCell(String text, String href) {
            super(CellType.LINK);
            this.text = text;
            this.href = href;
        }

        
    }

    private GroupingByField type;
    private Long id;
    private List<Cell> names;
    private Double time;
    private boolean actual;
    private List<GroupingReportDto> subGroup;

}
