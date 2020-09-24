package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the admin database table.
 * 
 */
@Entity
@Table(name="admin")
@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a")
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_admin", unique=true, nullable=false)
	private Integer idfAdmin;

	@Column(nullable=false, length=10)
	private String authority;

	@Column(nullable=false, length=50)
	private String email;

	@Column(nullable=false, length=11)
	private String mobile;

	@Column(nullable=false, length=20)
	private String name;

	@Column(nullable=false, length=64)
	private String password;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to NoticeNew
	@OneToMany(mappedBy="admin")
	private List<NoticeNew> noticeNews;

	//bi-directional many-to-one association to NoticeSystem
	@OneToMany(mappedBy="admin")
	private List<NoticeSystem> noticeSystems;

	public Admin() {
	}

	public Integer getIdfAdmin() {
		return this.idfAdmin;
	}

	public void setIdfAdmin(Integer idfAdmin) {
		this.idfAdmin = idfAdmin;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public List<NoticeNew> getNoticeNews() {
		return this.noticeNews;
	}

	public void setNoticeNews(List<NoticeNew> noticeNews) {
		this.noticeNews = noticeNews;
	}

	public NoticeNew addNoticeNew(NoticeNew noticeNew) {
		getNoticeNews().add(noticeNew);
		noticeNew.setAdmin(this);

		return noticeNew;
	}

	public NoticeNew removeNoticeNew(NoticeNew noticeNew) {
		getNoticeNews().remove(noticeNew);
		noticeNew.setAdmin(null);

		return noticeNew;
	}

	public List<NoticeSystem> getNoticeSystems() {
		return this.noticeSystems;
	}

	public void setNoticeSystems(List<NoticeSystem> noticeSystems) {
		this.noticeSystems = noticeSystems;
	}

	public NoticeSystem addNoticeSystem(NoticeSystem noticeSystem) {
		getNoticeSystems().add(noticeSystem);
		noticeSystem.setAdmin(this);

		return noticeSystem;
	}

	public NoticeSystem removeNoticeSystem(NoticeSystem noticeSystem) {
		getNoticeSystems().remove(noticeSystem);
		noticeSystem.setAdmin(null);

		return noticeSystem;
	}

}