package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Compressor {

    public void zip(File inputFile, File outputFile) {
        List<String> inputText = readFile(inputFile);
        List<String> compressedText = new ArrayList<>();
        for (String string : inputText) {
            compressedText.add(getRLE(string));
        }
        writeToFile(compressedText, outputFile);
    }

    public void unzip(File inputFile, File outputFile) {
        List<String> inputText = readFile(inputFile);
        List<String> decompressedText = new ArrayList<>();
        for (String string : inputText) {
            decompressedText.add(getDecompressedString(string).trim());
        }
        writeToFile(decompressedText, outputFile);
    }

    public List<String> readFile(File file) {
        List<String> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            if (line != null) {
                while (line != null) {
                    list.add(line);
                    line = bufferedReader.readLine();


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void writeToFile(List<String> text, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            StringBuilder sb = new StringBuilder();
            for (String string : text) {
                sb.append(string);
                sb.append("\n");
            }
            bufferedWriter.write(sb.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRLE(String str) {
        if (str == null || str.equals("")) {
            return str;
        }
        char currentChar = str.charAt(0);
        int currentCharCount = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= str.length(); i++) {
            char c;
            if (i < str.length()) {
                c = str.charAt(i);
            } else c = 0;
            if (i == str.length() || c != currentChar) {

                if (currentCharCount > 1) {
                    if (currentCharCount > 9) {
                        if (currentCharCount > 99) {
                            int digitsLen = String.valueOf(currentCharCount).length();
                            int p = digitsLen - 1;
                            StringBuilder charBuilder = new StringBuilder();
                            for (int j = 0; j < digitsLen; j++) {
                                p -= j;

                                int digit = (int) (currentCharCount / Math.pow(10, p)) % 10;
                                charBuilder.append((char) (digit + '0'));

                            }
                            sb.append(charBuilder);
                        } else {
                            int c1 = currentCharCount / 10;
                            int c2 = currentCharCount % 10;
                            sb.append((char) (c1 + '0')).append((char) (c2 + '0'));
                        }
                    } else {

                        sb.append((char) (currentCharCount + '0'));
                    }
                }
                sb.append(currentChar);
                currentCharCount = 1;
                currentChar = c;
            } else {
                currentCharCount++;
            }
        }
        return sb.toString();
    }

    private int getCharCount(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                count++;
            } else if (count > 0) break;
        }
        return count;
    }

    public String getDecompressedString(String compressedText) {
        char c = '0';
        String temp;
        if (compressedText.length() != 1) {
            c = compressedText.charAt(getCharCount(compressedText));
        }
        if (compressedText.length() == 1) {
            return compressedText;
        } else if (compressedText.charAt(0) == '0' && compressedText.length() != 2) {
            return getDecompressedString(compressedText.substring(2));
        } else if (compressedText.charAt(0) == '0' && compressedText.length() == 2) {
            compressedText = "\0";
            return compressedText;
        } else if (Character.isDigit(compressedText.charAt(0))) {
            int i = Integer.parseInt(compressedText.substring(0, getCharCount(compressedText)));
            i = i - 1;
            temp = Integer.toString(i);
            int k = 0;
            if (i == 9) k = 1;
            return c + getDecompressedString(temp.concat(compressedText.substring(getCharCount(temp) + k)));
        }

        return compressedText.charAt(0) + getDecompressedString(compressedText.substring(1));
    }

    public int getDecompressedLength(String compressedString) {
        int length = 0;
        int chCount = 0;
        for (int i = 0; i < compressedString.length(); i++) {
            char c = compressedString.charAt(i);
            if (Character.isDigit(c)) {

                chCount = chCount * (int) Math.pow(10, String.valueOf(chCount).length()) + Integer.parseInt(c + "");

            } else {
                length += chCount;
                chCount = 0;
                length++;
            }
        }

        return length;
    }

}
