package com.scd.gitlabtimeback.fileFillers;

import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.enums.GroupingByField;

import java.nio.charset.StandardCharsets;
import org.decimal4j.util.DoubleRounder;
import java.util.List;

@Component
public class CsvFiller {
    public byte[] fillFile(List<GroupingByField> grouping, List<GroupingReportDto> data) {
        return (fillHeader(grouping) + fillBody(grouping, data, 0) + fillFooter(data, grouping.size()))
                .replace("\t", " ")
                .getBytes(StandardCharsets.UTF_8);
    }

    private String fillHeader(List<GroupingByField> grouping) {
        StringBuilder str = new StringBuilder();

        grouping.forEach(resultEntry ->
                str.append(resultEntry.toString().concat(", ")));

        str.append("HOURS\n");

        return str.toString();
    }

    private String fillBody(List<GroupingByField> grouping, List<GroupingReportDto> data, Integer level) {

        if (data.isEmpty()) {
            return "";
        }

        var str = new StringBuilder();

        data.forEach(row -> {
            str.append(", ".repeat(Math.max(0, level)));
            str.append(row.getName().replace(",", " ").concat(", "));
            str.append(", ".repeat(Math.max(0, grouping.size() - level - 1)));
            str.append(Double.toString(DoubleRounder.round(row.getTime(),2)).concat("\n"));
            str.append(fillBody(grouping, row.getSubGroup(), level + 1));
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
