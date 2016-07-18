package swmaestro.lightsoo.game.Data;

/**
 * anni 테이블 컬럼
 * 기념일을 저장한다.
 */


public class Anni {

    private int event_id;
    private String event_title;
    private String event_date;


//    private String event_dday;

    public Anni(int event_id, String event_title, String event_date){
        this.event_id = event_id;
        this.event_title = event_title;
        this.event_date = event_date;
    }

    public void setTitle(String event_title){
        this.event_title = event_title;
    }

    public void setDate(String event_date){
        this.event_date = event_date;
    }

    public void setId(int event_id){
        this.event_id = event_id;
    }

    public String getTitle(){
        return event_title;
    }
    public String getDate(){
        return event_date;
    }
    public int getId(){
        return event_id;
    }

}
