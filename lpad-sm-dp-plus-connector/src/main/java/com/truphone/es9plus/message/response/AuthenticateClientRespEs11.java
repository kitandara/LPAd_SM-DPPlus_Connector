package com.truphone.es9plus.message.response;

public class AuthenticateClientRespEs11 {

  private String transactionId;

  private EventEntries[] eventEntries;

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public EventEntries[] getEventEntries() {
    return eventEntries;
  }

  public void setEventEntries(EventEntries[] eventEntries) {
    this.eventEntries = eventEntries;
  }

  public static class EventEntries {

    private String rspServerAddress;
    private String eventId;

    public String getRspServerAddress() {
      return rspServerAddress;
    }

    public void setRspServerAddress(String rspServerAddress) {
      this.rspServerAddress = rspServerAddress;
    }

    public String getEventId() {
      return eventId;
    }

    public void setEventId(String eventId) {
      this.eventId = eventId;
    }
  }

}
