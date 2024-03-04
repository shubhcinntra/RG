package com.cinntra.rgelectric.newapimodel;

public class DataReceivableGraph {
    public int id;

    public double TotalDue;

    public String OverDueDaysGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalDue() {
        return TotalDue;
    }

    public void setTotalDue(double totalDue) {
        TotalDue = totalDue;
    }

    public String getOverDueDaysGroup() {
        return OverDueDaysGroup;
    }

    public void setOverDueDaysGroup(String overDueDaysGroup) {
        OverDueDaysGroup = overDueDaysGroup;
    }
}
