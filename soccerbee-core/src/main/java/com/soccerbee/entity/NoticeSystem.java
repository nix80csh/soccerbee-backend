package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the notice_system database table.
 * 
 */
@Entity
@Table(name="notice_system")
@NamedQuery(name="NoticeSystem.findAll", query="SELECT n FROM NoticeSystem n")
public class NoticeSystem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_notice_system", unique=true, nullable=false)
	private int idfNoticeSystem;

	@Column(length=500)
	private String content;

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

	public NoticeSystem() {
	}

	public int getIdfNoticeSystem() {
		return this.idfNoticeSystem;
	}

	public void setIdfNoticeSystem(int idfNoticeSystem) {
		this.idfNoticeSystem = idfNoticeSystem;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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