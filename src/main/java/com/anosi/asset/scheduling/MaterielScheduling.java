package com.anosi.asset.scheduling;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.anosi.asset.service.MaterielService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class MaterielScheduling {

	@Autowired
	private MaterielService materielService;
	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	/***
	 * 每天0点进行预测性维护检测
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ParseException
	 * @throws MalformedTemplateNameException
	 * @throws TemplateNotFoundException
	 * @throws TemplateException
	 */
	// @Scheduled(cron = "0 0 0 * * *")
	public void checkCalculate() throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		// 基本设置.
		helper.setFrom(from);// 发送者.
		helper.setTo("jinyao@anosi.cn");// 接收者.
		helper.setSubject("模板邮件（邮件主题）");// 邮件主题.

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		// 设定去哪里读取相应的ftl模板
		cfg.setClassForTemplateLoading(this.getClass(), "/templates/mail/");
		// 在模板文件目录中寻找名称为name的模板文件
		Template template = cfg.getTemplate("email.ftl");

		// 获取需要发送的邮件内容
		List<Map<String, Object>> models = materielService.findAll().parallelStream().map(ma -> {
			// 判断是否预警,如果已经到了需要预警的天数,则发邮件
			if (ma.needRemind()) {
				Map<String, Object> model = new HashMap<String, Object>();
				return model;
			}
			return null;
		}).filter(message -> message != null).collect(Collectors.toList());

		// 发送邮件
		for (Map<String, Object> model : models) {
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			helper.setText(html, true);
			mailSender.send(mimeMessage);
		}
	}

}
