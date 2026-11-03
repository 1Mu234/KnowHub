package com.knowhub.util;

/**
 * 从文档正文中提取「命中关键词的那一句话」，用于检索结果展示。
 * 返回结果已做 HTML 转义，关键词用 {@code <mark>} 包裹，可直接在页面输出。
 */
public class SnippetUtil {

    /** 片段最大长度，超出时以关键词为中心截取窗口。 */
    private static final int MAX_LEN = 160;

    private static final String BOUNDARIES = "。！？；!?;\n\r";

    /**
     * 提取关键词所在的句子。
     * @param content 文档正文
     * @param keyword 检索关键词（为空时返回开头一段）
     */
    public static String extract(String content, String keyword) {
        if (content == null || content.isBlank()) return "";

        String text = content.replaceAll("\\s+", " ").trim();
        if (keyword == null || keyword.isBlank()) {
            return escape(truncate(text, 0, MAX_LEN));
        }

        int idx = text.toLowerCase().indexOf(keyword.toLowerCase());
        if (idx < 0) {
            // 正文没命中（可能只在标题命中），展示开头
            return escape(truncate(text, 0, MAX_LEN));
        }

        // 向前后找句子边界
        int start = idx;
        while (start > 0 && BOUNDARIES.indexOf(text.charAt(start - 1)) < 0) start--;
        int end = idx + keyword.length();
        while (end < text.length() && BOUNDARIES.indexOf(text.charAt(end)) < 0) end++;

        String sentence = text.substring(start, end).trim();

        // 句子过长时，以关键词为中心裁剪窗口
        if (sentence.length() > MAX_LEN) {
            int kwPos = idx - start;
            int from = Math.max(0, kwPos - MAX_LEN / 3);
            int to = Math.min(sentence.length(), from + MAX_LEN);
            from = Math.max(0, to - MAX_LEN);
            sentence = (from > 0 ? "…" : "") + sentence.substring(from, to)
                    + (to < sentence.length() ? "…" : "");
        }

        return highlight(sentence, keyword);
    }

    private static String truncate(String text, int from, int max) {
        if (text.length() <= max) return text;
        return text.substring(from, Math.min(text.length(), from + max)) + "…";
    }

    /** 先转义 HTML，再用 &lt;mark&gt; 包裹关键词（大小写不敏感）。 */
    private static String highlight(String sentence, String keyword) {
        StringBuilder sb = new StringBuilder();
        String lowerSentence = sentence.toLowerCase();
        String lowerKw = keyword.toLowerCase();
        int pos = 0;
        while (true) {
            int i = lowerSentence.indexOf(lowerKw, pos);
            if (i < 0) {
                sb.append(escape(sentence.substring(pos)));
                break;
            }
            sb.append(escape(sentence.substring(pos, i)));
            int end = Math.min(i + keyword.length(), sentence.length());
            sb.append("<mark>").append(escape(sentence.substring(i, end))).append("</mark>");
            pos = end;
        }
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
