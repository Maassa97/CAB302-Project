package com.cab302.javafxreadingdemo.model;

/** Role:
 * -weight % share of subject
 * -mark achieved (0-100)
 * -contribution (mark*weight)/100 used for subject sum %
 */
public class Assessment {
    private int id;
    private int subjectId;
    private String name;
    private double weight;
    private double mark;

    public Assessment(int id, int subjectId, String name, double weight, double mark) {
        this.id = id; this.subjectId = subjectId; this.name = name; this.weight = weight; this.mark = mark;
    }
    public Assessment(int subjectId, String name, double weight, double mark) {
        this.subjectId = subjectId; this.name = name; this.weight = weight; this.mark = mark;
    }

    public int getId() { return id; }
    public int getSubjectId() { return subjectId; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getMark() { return mark; }

    public void setId(int id) { this.id = id; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setMark(double mark) { this.mark = mark; }

    // weighted mark for subject %
    public double getContribution() { return (mark * weight) / 100.0; }
}
