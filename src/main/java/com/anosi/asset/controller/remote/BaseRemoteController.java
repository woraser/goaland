package com.anosi.asset.controller.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.anosi.asset.component.RemoteComponent;
import com.anosi.asset.controller.BaseController;
import com.anosi.asset.model.jpa.BaseEntity;

@RestController
public class BaseRemoteController extends BaseController<BaseEntity> {

	@Autowired
	protected RemoteComponent remoteComponent;

}
