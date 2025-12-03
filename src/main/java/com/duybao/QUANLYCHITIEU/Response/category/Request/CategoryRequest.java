package com.duybao.QUANLYCHITIEU.Response.category.Request;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.text.Normalizer;
import java.util.regex.Pattern;
@Data
public class CategoryRequest {
    @NotBlank
    private String name;

    @NotNull
    private TransactionType type; // EXPENSE hoặc INCOME

    private String iconUrl;
    private String color;


    private static final Pattern MULTI_SPACE = Pattern.compile("\\s+");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public String normalizeName(String raw, boolean removeDiacritics) {
        if (raw == null) return null;
        String s = raw.trim();
        s = MULTI_SPACE.matcher(s).replaceAll(" ");
        s = Normalizer.normalize(s, Normalizer.Form.NFC);
        if (removeDiacritics) {
            s = Normalizer.normalize(s, Normalizer.Form.NFD);
            s = DIACRITICS.matcher(s).replaceAll("");
            s = Normalizer.normalize(s, Normalizer.Form.NFC);
        }
        return s.toLowerCase();
    }
}
