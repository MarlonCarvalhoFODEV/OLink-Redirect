package Itens;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ITPartner implements Serializable {

    private int id, registers, subscribers, cancels, clicks;
    private double revenue, revenueMonthly, conversions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegisters() {
        return registers;
    }

    public void setRegisters(int registers) {
        this.registers = registers;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    public double getConversions() {
        DecimalFormat format = new DecimalFormat("0.##");
        return Double.valueOf(format.format(conversions).replace(",","."));
    }

    public void setConversions(double conversions) {
        this.conversions = conversions;
    }

    public int getCancels() {
        return cancels;
    }

    public void setCancels(int cancels) {
        this.cancels = cancels;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getRevenueMonthly() {
        return revenueMonthly;
    }

    public void setRevenueMonthly(double revenueMonthly) {
        this.revenueMonthly = revenueMonthly;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
