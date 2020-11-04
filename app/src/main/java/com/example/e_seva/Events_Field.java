package com.example.e_seva;

public class Events_Field {
    private String Date;
    private String Organizer;
    private String Venue;
    private String Description;
    private String Event_image;
    private String Event_title;

private Events_Field(){}

private Events_Field(String Date,String Organizer,String Venue,String Description,String Event_image,String Event_title){

    this.Date=Date;
    this.Organizer=Organizer;
    this.Venue=Venue;
    this.Description=Description;
    this.Event_image=Event_image;
    this.Event_title=Event_title;

}

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrganizer() {
        return Organizer;
    }

    public void setOrganizer(String organizer) {
        Organizer = organizer;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEvent_image() {
        return Event_image;
    }

    public void setEvent_image(String event_image) {
        Event_image = event_image;
    }

    public String getEvent_title() {
        return Event_title;
    }

    public void setEvent_title(String event_title) {
        Event_title = event_title;
    }
}
