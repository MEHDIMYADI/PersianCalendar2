package ir.dimyadi.persiancalendar.view.testament;

public class MyTestament {

    private String title;
    private String note;
    private String dt;

    public MyTestament(){}

    public MyTestament(String title, String note, String dt){
        this.title = title;
        this.note = note;
        this.dt = dt;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getDt() {
        return dt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
