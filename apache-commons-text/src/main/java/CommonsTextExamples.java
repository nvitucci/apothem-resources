import org.apache.commons.text.*;
import org.apache.commons.text.diff.*;
import org.apache.commons.text.similarity.*;
import org.apache.commons.text.translate.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ShowVisitor<Character> implements CommandVisitor<Character> {
    private int inserts = 0;
    private int keeps = 0;
    private int deletes = 0;

    public void visitInsertCommand(Character character) {
        ++inserts;
        System.out.println(String.format("insert %s", character));
    }

    public void visitKeepCommand(Character character) {
        ++keeps;
        System.out.println(String.format("keep   %s", character));
    }

    public void visitDeleteCommand(Character character) {
        ++deletes;
        System.out.println(String.format("delete %s", character));
    }

    public void printStats() {
        System.out.println(String.format("%d inserts, %d deletes, %d keeps", inserts, deletes, keeps));
    }
}

public class CommonsTextExamples {
    public static void main(String[] args) {
        caseUtilsExample();
        stringEscapeUtilsExample();
        stringSubstitutorExample();
        wordUtilsExample();

        diffExample();
        translateExample();

        similaritiesExample();
        sentenceSimilarityExample();
        distancesExample();
        sentenceDistanceExample();
    }

    private static void printExampleHeader(String example) {
        // Contains an example of TextStringBuilder
        String header = "Examples of " + example;
        System.out.println("\n" + header);

        TextStringBuilder builder = new TextStringBuilder();
        System.out.println(builder.appendPadding(header.length(), '-').toString());
    }

    public static void caseUtilsExample() {
        printExampleHeader("CaseUtils");

        String string = "java-programming-language";

        System.out.println(CaseUtils.toCamelCase(string, true, '-'));
        System.out.println(CaseUtils.toCamelCase(string, false, '-'));
    }

    public static void stringEscapeUtilsExample() {
        printExampleHeader("StringEscapeUtils");

        String string = "<a>Department, R&D</a>";

        System.out.println(StringEscapeUtils.escapeHtml4(string));
        System.out.println(StringEscapeUtils.escapeXml11(string));
        System.out.println(StringEscapeUtils.escapeCsv(string));

        System.out.println(
                StringEscapeUtils
                        .builder(StringEscapeUtils.ESCAPE_HTML4)
                        .append("R&D dept: ")
                        .escape(string)
                        .toString());
    }

    public static void stringSubstitutorExample() {
        printExampleHeader("StringSubstitutor");

        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("city", "London");
        substitutions.put("country", "England");

        // With static method
        System.out.println(
                StringSubstitutor.replace("${city} is the capital of ${country}", substitutions));

        // With StringSubstitutor object
        StringSubstitutor sub = new StringSubstitutor(substitutions);
        System.out.println(sub.replace("${city} is the capital of ${country}"));

        StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        System.out.println(interpolator.replace("Base64 encoder: ${base64Encoder:Secret password}"));
    }

    public static void wordUtilsExample() {
        printExampleHeader("WordUtils");

        String longString = "This is a very long string, from https://www.example.org";
        String allLower = "all lower but ONE";
        String allCapitalized = "All Capitalized But ONE";

        System.out.println("\nWordUtils: Abbreviation");
        // Take at least 9 characters, cutting to 12 characters if no space is found before
        System.out.println(WordUtils.abbreviate(longString, 9, 12, " ..."));
        // Take at least 10 characters, cutting to 12 characters if no space is found before
        System.out.println(WordUtils.abbreviate(longString, 10, 12, " ..."));
        // Take at least 10 characters, then cut on the first space wherever it is
        System.out.println(WordUtils.abbreviate(longString, 10, -1, " ..."));

        System.out.println("\nWordUtils: Initials");
        System.out.println(WordUtils.initials(allLower));
        System.out.println(WordUtils.initials(allCapitalized));

        System.out.println("\nWordUtils: Case change");
        // Doesn't lowercase the uppercase characters
        System.out.println(WordUtils.capitalize(allLower));
        // Lowercases everything, then capitalizes the first letter of each word
        System.out.println(WordUtils.capitalizeFully(allLower));
        // Lowercases the first letter of each word
        System.out.println(WordUtils.uncapitalize(allCapitalized));
        // Swaps the case of each character
        System.out.println(WordUtils.swapCase(allLower));

        System.out.println("\nWordUtils: Wrapping");
        // Line length is 10, uses '\n' as a line break, does not break words longer than the line
        System.out.println(
                WordUtils.wrap(longString, 10, "\n", false) + "\n");

        // Line length is 10, uses '\n' as a line break, breaks words longer than the line
        System.out.println(
                WordUtils.wrap(longString, 10, "\n", true) + "\n");

        // Line length is 10, uses '\n' as a line break, breaks words longer than the line, also breaks on commas
        System.out.println(
                WordUtils.wrap(longString, 10, "\n", true, ",") + "\n");
    }

    public static void diffExample() {
        printExampleHeader("diff");

        String s1 = "hyperspace";
        String s2 = "cyberscape";

        StringsComparator comparator = new StringsComparator(s1, s2);
        EditScript<Character> script = comparator.getScript();

        System.out.println("Longest Common Subsequence length (number of \"keep\" commands): " +
                script.getLCSLength());
        System.out.println("Effective modifications (number of \"insert\" and \"delete\" commands): " +
                script.getModifications());

        ShowVisitor<Character> visitor = new ShowVisitor<>();
        script.visit(visitor);
        visitor.printStats();
    }

    public static void translateExample() {
        printExampleHeader("translate");

        Map<CharSequence, CharSequence> translation = new HashMap<>();
        translation.put("e", "3");
        translation.put("l", "1");
        translation.put("t", "7");

        String s1 = "Let it be!";

        LookupTranslator lookupTranslator = new LookupTranslator(translation);
        System.out.println(lookupTranslator.translate(s1));

        UnicodeEscaper unicodeEscaper = new UnicodeEscaper();
        UnicodeUnescaper unicodeUnescaper = new UnicodeUnescaper();

        String unicodeString = unicodeEscaper.translate(s1);
        System.out.println(unicodeString);
        System.out.println(unicodeUnescaper.translate(unicodeString));
    }

    public static void similaritiesExample() {
        printExampleHeader("similarities");

        String s1 = "hyperspace";
        String s2 = "cyberscape";

        JaccardSimilarity jaccard = new JaccardSimilarity();
        System.out.println("Jaccard similarity: " + jaccard.apply(s1, s2));

        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();
        System.out.println("Jaro-Winkler similarity: " + jaroWinkler.apply(s1, s2));

        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        System.out.println("Longest Common Subsequence similarity: " + lcs.apply(s1, s2));

        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        System.out.println("Fuzzy score similarity: " + fuzzyScore.fuzzyScore(s1, s2));
        System.out.println("Fuzzy score similarity: " + fuzzyScore.fuzzyScore(s1, "space"));
    }

    public static void sentenceSimilarityExample() {
        printExampleHeader("sentence similarity");

        String s1 = "string similarity";
        String s2 = "string distance";

        Map<CharSequence, Integer> vector1 = new HashMap<>();
        Map<CharSequence, Integer> vector2 = new HashMap<>();

        for (String token : s1.split(" ")) {
            vector1.put(token, vector1.getOrDefault(token, 0) + 1);
        }

        for (String token : s2.split(" ")) {
            vector2.put(token, vector2.getOrDefault(token, 0) + 1);
        }

        CosineSimilarity cosine = new CosineSimilarity();
        System.out.println("Cosine similarity: " + cosine.cosineSimilarity(vector1, vector2));

        // Adding one repetition of "string" to vector2
        vector2.put("string", vector2.getOrDefault("string", 0) + 1);
        System.out.println("Cosine similarity: " + cosine.cosineSimilarity(vector1, vector2));
    }

    public static void distancesExample() {
        printExampleHeader("distances");

        String s1 = "hyperspace";
        String s2 = "cyberscape";

        HammingDistance hamming = new HammingDistance();
        // Requires the two strings to have the same length
        System.out.println("Hamming distance: " + hamming.apply(s1, s2));

        JaccardDistance jaccard = new JaccardDistance();
        System.out.println("Jaccard distance: " + jaccard.apply(s1, s2));

        JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();
        // The result is wrong at the moment (see https://issues.apache.org/jira/browse/TEXT-104)
        System.out.println("Jaro-Winkler distance: " + jaroWinkler.apply(s1, s2));

        LongestCommonSubsequenceDistance lcs = new LongestCommonSubsequenceDistance();
        System.out.println("Longest Common Subsequence distance: " + lcs.apply(s1, s2));

        LevenshteinDistance levenshtein = new LevenshteinDistance();
        System.out.println("Levenshtein distance: " + levenshtein.apply(s1, s2));

        LevenshteinDistance levenshteinWithThreshold = new LevenshteinDistance(3);
        // Returns -1 since the actual distance, 4, is higher than the threshold
        System.out.println("Levenshtein distance: " + levenshteinWithThreshold.apply(s1, s2));

        LevenshteinDetailedDistance levenshteinDetailed = new LevenshteinDetailedDistance();
        System.out.println("Levenshtein detailed distance: " + levenshteinDetailed.apply(s1, s2));
    }

    public static void sentenceDistanceExample() {
        printExampleHeader("sentence distance");

        String s1 = "string similarity";
        String s2 = "string distance";

        CosineDistance cosine = new CosineDistance();
        System.out.println("Cosine distance: " + cosine.apply(s1, s2));
        System.out.println("Cosine distance: " + cosine.apply(s1, s2 + " string"));
    }
}
