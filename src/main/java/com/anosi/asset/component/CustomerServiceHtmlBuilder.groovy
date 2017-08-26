package com.anosi.asset.component

import org.springframework.stereotype.Component

import com.alibaba.fastjson.JSONObject
import com.anosi.asset.model.jpa.CustomerServiceProcess

import groovy.xml.MarkupBuilder

@Component
class CustomerServiceHtmlBuilder {

	String convertToProcessHtml(Iterable<CustomerServiceProcess> processes){
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)

		processes.each{
			convertStartedProcessHtml(it, builder)
		}

		return writer.toString()
	}

	private void convertStartedProcessHtml(CustomerServiceProcess csp,MarkupBuilder builder) {
		builder.div(class:"taskModel"){
			div(class:"taskAllSimple"){
				div(class:"taskSimple",style:"color:#ffff;text-align: center;width:18%;height: 100%;","工单号:" + csp.name){}
				div(class:"taskSimple",style:"color:#ABADB3;width:25%;height: 100%;","发起时间:" + csp.historicProcessInstance.startTime + "    申请人:" + csp.applicant.name){}
			}
			div(class:"taskAll"){
				div(class:"taskPicture"){
					img(src:"/webResources/img/process/22.png",style:"width: 75%;height: 60%;margin-top: 22%;margin-left: 30%;")
				}
				div(class:"deviceTable"){
					table(class:"table"){
						tbody(){
							tr(){
								td(class:"deviceInfo","设备名称"){}
								td(class:"deviceNewInfo",csp.startDetail.projectName){}
							}
							tr(){
								td(class:"deviceInfo","安装位置"){}
								td(class:"deviceNewInfo",csp.startDetail.projectLocation){}
							}
							tr(){
								td(class:"deviceInfo","使用客户"){}
								td(class:"deviceNewInfo","国家电网公司哈密南800AV换流站"){}
							}
						}
					}
				}
				div(class:"describeDevice",csp.startDetail.sceneDescription){}
				div(class:"taskState"){
					div(class:"state",csp.finishType.name){}
					div(class:"taskData","2017-05-23"){}
				}
				div(class:"taskButton"){
					button(class:"doButton","详情"){}
				}
			}
		}
	}
}
