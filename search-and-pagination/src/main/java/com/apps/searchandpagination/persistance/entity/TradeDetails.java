package com.apps.searchandpagination.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TRADE_DETAILS")
public class TradeDetails {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "GROUP")
    private Integer group;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "DOMAIN")
    private String domain;
    @Column(name = "SITE")
    private String site;
    @Column(name = "SYSTEM_USE")
    private String systemUse;
    @Column(name = "CLAZZ")
    private Integer clazz;
    @Column(name = "RATE")
    private Integer rate;
    @Column(name = "EDUCATION")
    private String education;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "QUALIFICATIONS")
    private String qualifications;
    @Column(name = "ABSOLUTE")
    private String absolute;
    @Column(name = "CITIZENSHIP")
    private String citizenship;
    @Column(name = "REFERENCE")
    private String reference;
    @Column(name = "REPORT_ID")
    private String reportId;
    @Column(name = "CASH")
    private String cash;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "IBAN")
    private String iban;
    @Column(name = "BIC")
    private String bic;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "MIX")
    private String mix;
    @Lob
    @Column(name = "DETAILS")
    private String details;

    public TradeDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSystemUse() {
        return systemUse;
    }

    public void setSystemUse(String systemUse) {
        this.systemUse = systemUse;
    }

    public Integer getClazz() {
        return clazz;
    }

    public void setClazz(Integer clazz) {
        this.clazz = clazz;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getAbsolute() {
        return absolute;
    }

    public void setAbsolute(String absolute) {
        this.absolute = absolute;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMix() {
        return mix;
    }

    public void setMix(String mix) {
        this.mix = mix;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}