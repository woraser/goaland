package com.anosi.asset.model.jpa;

import com.alibaba.fastjson.annotation.JSONField;
import com.anosi.asset.model.elasticsearch.Content;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customerServiceProcess")
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class CustomerServiceProcess extends BaseProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;

	@Content(extractFields = { "applicant.name" })
	@IndexedEmbedded(depth = 1)
	private Account applicant;// 发起人

    @IndexedEmbedded
	private StartDetail startDetail;// 发起字段

	private ExamineDetail examineDetail = new ExamineDetail();// 领导审批字段

	private EvaluatingDetail evaluatingDetail;// 评估字段

	private DistributeDetail distributeDetail;// 派单字段

	private RepairDetail repairDetail;// 维修字段
	
	private EntrustDetail entrustDetail;// 转派字段

	private AgreementStatus agreementStatus;// 合同状态

	private boolean file = false;// 是否有上传文件

	@JSONField(serialize=false)  
	private List<CustomerServiceProcessDailyPer> completedPerList = new ArrayList<>();

	@JSONField(serialize=false)  
	private List<CustomerServiceProcessDailyPer> unCompletedPerList = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "completedProcessList", targetEntity = CustomerServiceProcessDailyPer.class)
	public List<CustomerServiceProcessDailyPer> getCompletedPerList() {
		return completedPerList;
	}

	public void setCompletedPerList(List<CustomerServiceProcessDailyPer> completedPerList) {
		this.completedPerList = completedPerList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "unCompletedProcessList", targetEntity = CustomerServiceProcessDailyPer.class)
	public List<CustomerServiceProcessDailyPer> getUnCompletedPerList() {
		return unCompletedPerList;
	}

	public void setUnCompletedPerList(List<CustomerServiceProcessDailyPer> unCompletedPerList) {
		this.unCompletedPerList = unCompletedPerList;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	@JoinColumn(nullable = false)
	public Account getApplicant() {
		return applicant;
	}

	public void setApplicant(Account applicant) {
		this.applicant = applicant;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public StartDetail getStartDetail() {
		return startDetail;
	}

	public void setStartDetail(StartDetail startDetail) {
		this.startDetail = startDetail;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public RepairDetail getRepairDetail() {
		return repairDetail;
	}

	public void setRepairDetail(RepairDetail repairDetail) {
		this.repairDetail = repairDetail;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public EvaluatingDetail getEvaluatingDetail() {
		return evaluatingDetail;
	}

	public void setEvaluatingDetail(EvaluatingDetail evaluatingDetail) {
		this.evaluatingDetail = evaluatingDetail;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public ExamineDetail getExamineDetail() {
		return examineDetail;
	}

	public void setExamineDetail(ExamineDetail examineDetail) {
		this.examineDetail = examineDetail;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public AgreementStatus getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(AgreementStatus agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public DistributeDetail getDistributeDetail() {
		return distributeDetail;
	}

	public void setDistributeDetail(DistributeDetail distributeDetail) {
		this.distributeDetail = distributeDetail;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	public EntrustDetail getEntrustDetail() {
		return entrustDetail;
	}

	public void setEntrustDetail(EntrustDetail entrustDetail) {
		this.entrustDetail = entrustDetail;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

}
