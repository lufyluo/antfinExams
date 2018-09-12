package com.antfin.exam.filereader.Models;

public class TestData implements Comparable {
    private String id;
    private String groupId;
    private float quota;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }
    public TestData deserialize(String data){
        String[] temp = data.split(",");
        this.id = temp[0];
        this.groupId = temp[1];
        this.quota = Float.parseFloat(temp[2]);
        return this;
    }

    @Override
    public int compareTo(Object o) {
        TestData testData = (TestData)o;
        return Integer.parseInt(this.groupId)-Integer.parseInt(testData.groupId);
    }
    @Override
    public String toString(){
        return this.getGroupId()+"，"+this.getId()+"，"+this.getQuota();
    }
}
