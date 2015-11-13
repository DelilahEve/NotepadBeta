package ca.delilaheve.notepad.util;

import java.util.ArrayList;

public class ColourPack {

    private int r;
    private int g;
    private int b;

    private String hex;

    public ColourPack(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        convert(r, g, b);
    }

    public ColourPack(String hex) {
        this.hex = hex;
        convert(hex);
    }

    public int red() {
        return r;
    }

    public int green() {
        return g;
    }

    public int blue() {
        return b;
    }

    public void setColour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        convert(r, g, b);
    }

    public void setColour(String hex) {
        this.hex = hex;
        convert(hex);
    }

    public int[] asRGB() {
        return new int[] {r, g, b};
    }

    public String asHex() {
        return hex;
    }

    private void convert(String hex) {
        this.r = hexToDec(hex.substring(0, 2));
        this.g = hexToDec(hex.substring(2, 4));
        this.b = hexToDec(hex.substring(4));
    }

    private void convert(int r, int g, int b) {
        this.hex = String.format("%s%s%s", decToHex(r), decToHex(g), decToHex(b));
    }

    private String decToHex(int dec){
        String hex = "";

        while (dec > 0){
            hex += Res.HEX_NUMBERS.charAt(dec%16);
            dec = dec/16;
        }

        return hex;
    }

    private int hexToDec(String hex) {
        System.out.println(hex + ":");
        int dec = 0;
        ArrayList<String> characters = new ArrayList<>();

        while(!hex.equals("")){
            characters.add(hex.substring(hex.length()-1));
            hex = hex.substring(0, hex.length()-1);
        }

        ArrayList<Integer> decValues = new ArrayList<>();

        for(String c : characters) {
            decValues.add(Res.HEX_NUMBERS.indexOf(c));
        }

        int i = 0;
        for(int d : decValues) {
            dec += (d * (Math.pow(16, i)));
            System.out.println("dec: " + dec);
            i++;
        }

        return dec;
    }

    public ColourPack textColour(){
        int d = 0;

        double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b)/255;

        if(a > 0.5)
            d = 255;

        return new ColourPack(d, d, d);
    }
}
