package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the notice_new database table.
 * 
 */
@Entity
@Table(name="notice_new")
@NamedQuery(name="NoticeNew.findAll", query="SELECT n FROM NoticeNew n")
public class NoticeNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_notice_new", unique=true, nullable=false)
	private int idfNoticeNew;

	@Column(length=2000)
	private String content;

	@Temporal(TemporalType.DATE)
	@Column(name="due_date")
	private Date dueDate;

	@Column(length=100)
	private String image;

	@Column(name="reg_date", insertable=false, updatable=false)
	private Timestamp regDate;

	@Column(length=50)
	private String title;

	@Column(name="upd_date", insertable=false, updatable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Admin
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_admin", nullable=false)
	private Admin admin;

	public NoticeNew() {
	}

	public int getIdfNoticeNew() {
		return this.idfNoticeNew;
	}

	public void setIdfNoticeNew(int idfNoticeNew) {
		this.idfNoticeNew = idfNoticeNew;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}