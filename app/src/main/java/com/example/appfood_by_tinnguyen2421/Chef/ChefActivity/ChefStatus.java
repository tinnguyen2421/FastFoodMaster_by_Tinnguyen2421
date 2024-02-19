package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

public class ChefStatus {
    private String Status,Reason,TimeEnd,TimeStart,TimeStop;

    public ChefStatus() {
        // Cần có constructor mặc định không làm gì cả
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeStop() {
        return TimeStop;
    }

    public void setTimeStop(String timeStop) {
        TimeStop = timeStop;
    }
}
