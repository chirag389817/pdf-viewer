package com.csp.pdfviewer.utilclasses;

import java.util.ArrayList;

public class PageSet {

    public static int TYPE_ALL = 201;
    public static int TYPE_RANGE = 202;
    public static int TYPE_CUSTOM = 203;

    int fromPage=0;
    int toPage=0;
    int typeCode=-1;

    String pdfName;
    ArrayList<Integer> selectedPages=null;

    public PageSet(PageSet old){
        this.fromPage=old.getFromPage();
        this.toPage= old.getToPage();
        this.typeCode= old.getTypeCode();
        this.pdfName= old.getPdfName();
        this.selectedPages= old.getSelectedPages();
    }

    public PageSet(String pdfName){
        typeCode=TYPE_ALL;
        this.pdfName=pdfName;
    }

    public PageSet(int fromPage, int toPage,String pdfName){
        this.typeCode=TYPE_RANGE;
        this.fromPage=fromPage;
        this.pdfName=pdfName;
        this.toPage=toPage;
    }

    public PageSet(ArrayList<Integer> selectedPages,String pdfName){
        typeCode=TYPE_CUSTOM;
        this.pdfName=pdfName;
        this.selectedPages=selectedPages;
    }

    public int getFromPage() {
        return fromPage;
    }

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public int getToPage() {
        return toPage;
    }

    public void setToPage(int toPage) {
        this.toPage = toPage;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public ArrayList<Integer> getSelectedPages() {
        return selectedPages;
    }

    public void setSelectedPages(ArrayList<Integer> selectedPages) {
        this.selectedPages = selectedPages;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

}
