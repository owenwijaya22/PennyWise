package pennywise.model;

import java.time.LocalDate;

public class Goal {
    private String description;
    private double targetAmount;
    private double currentAmount;
    private LocalDate deadline;
    private boolean isCompleted;

    public Goal(String description, double targetAmount, LocalDate deadline) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = 0.0;
        this.deadline = deadline;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void updateProgress(double amount) {
        this.currentAmount = amount;
        this.isCompleted = (currentAmount >= targetAmount);
    }

    public double getProgressPercentage() {
        return (currentAmount / targetAmount) * 100;
    }

    public boolean isOverdue() {
        return !isCompleted && LocalDate.now().isAfter(deadline);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
        this.isCompleted = (currentAmount >= targetAmount);
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}