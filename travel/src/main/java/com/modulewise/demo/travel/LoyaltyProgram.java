package com.modulewise.demo.travel;

public class LoyaltyProgram {

    private String programName;
    private String membershipNumber;
    private String tier;

    public LoyaltyProgram() {}

    public LoyaltyProgram(String programName, String membershipNumber, String tier) {
        this.programName = programName;
        this.membershipNumber = membershipNumber;
        this.tier = tier;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
