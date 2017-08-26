package com.anosi.asset.test

import org.junit.Test

import groovy.xml.MarkupBuilder

class TestHtml {

	@Test
	void testHtml(){
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		
		builder.html(){
		  head(){
			title("Groov'n with Builders"){}
		  }
		  body(){
			p("Welcome to Builders 101. As you can see " +
			  "this Groovlet is fairly simple.")
		  }
		}
		println writer.toString()
	}
	
	@Test
	void testHtml2(){
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		
		builder.div(class:"taskModel"){
			div(class:"taskAllSimple"){
				div(class:"taskSimple",style:"color:#ffff;text-align: center;width:18%;height: 100%;","工单号:123456"){}
				div(class:"taskSimple",style:"color:#ABADB3;width:25%;height: 100%;","发起时间:2017-05-22 09:35:15    申请人:abc"){}
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
								td(class:"deviceNewInfo","直流输电换流纯水冷设备"){}
							}
							tr(){
								td(class:"deviceInfo","安装位置"){}
								td(class:"deviceNewInfo","国网新疆电力公司哈密供电公司"){}
							}
							tr(){
								td(class:"deviceInfo","使用客户"){}
								td(class:"deviceNewInfo","国家电网公司哈密南800AV换流站"){}
							}
						}
					}
				}
				div(class:"describeDevice","齿轮泵变频器温度持续上升，导致变频器高温跳闸，齿轮泵变频器温度持续上升"){}
				div(class:"taskState"){
					div(class:"state","已完成"){}
					div(class:"taskData","2017-05-23"){}
				}
				div(class:"taskButton"){
					button(class:"doButton","详情"){}
				}
			}
		}
		
		println writer.toString()
	}
	
}
