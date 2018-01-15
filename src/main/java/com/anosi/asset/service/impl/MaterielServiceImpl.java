package com.anosi.asset.service.impl;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.MaterielDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.Materiel;
import com.anosi.asset.model.jpa.QMateriel;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.service.MaterielService;
import com.anosi.asset.util.DateFormatUtil;
import com.anosi.asset.util.ExcelUtil;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

@Service("materielService")
@Transactional
public class MaterielServiceImpl extends BaseJPAServiceImpl<Materiel> implements MaterielService {

    @Autowired
    private MaterielDao materielDao;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private EntityManager entityManager;

    @Override
    public BaseJPADao<Materiel> getRepository() {
        return materielDao;
    }

    @Override
    public Page<Materiel> findBySearchContent(String searchContent, String deviceSN, Pageable pageable) {
        return materielDao.findBySearchContent(entityManager, searchContent, pageable, deviceSN);
    }

    @Override
    public void batchSave(InputStream is) throws Exception {
        Table<Integer, String, Object> table = ExcelUtil.readExcel(is, 0);
        chekcMulitName(table);
        Map<Integer, Map<String, Object>> rows = table.rowMap();
        List<Materiel> materiels = new ArrayList<>();
        rows.forEach((rowNum, cells) -> {
            Materiel materiel = parseExcel(rowNum, cells);
            materiel.needRemind();
            materiels.add(materiel);
        });
        materielDao.save(materiels);
    }

    /**
     * 判断当前excel是否有重复且相同设备序列号的name
     *
     * @param table
     */
    private void chekcMulitName(Table<Integer, String, Object> table) {
        Map<Integer, Object> nameMap = table.columnMap().get("物料名称");
        Map<Integer, Object> serialNoMap = table.columnMap().get("所属设备(序列号)");
        int size = table.rowKeySet().size();
        for (int i = 0; i < size; i++) {
            Object nameI = nameMap.get(i);
            Object serialNoI = serialNoMap.get(i);
            for (int j = i + 1; j < size; j++) {
                Object nameJ = nameMap.get(j);
                if (Objects.equals(nameI, nameJ) && Objects.equals(serialNoI, serialNoMap.get(j))) {
                    throw new CustomRunTimeException(
                            MessageFormat.format(i18nComponent.getMessage("materiel.exist.excel"), i + 1, j + 1));
                }
            }
        }
    }

    /***
     * 将excel解析成bean
     *
     * @param rowNum
     * @param cells
     * @return
     */
    private Materiel parseExcel(int rowNum, Map<String, Object> cells) {
        String serialNo = cells.get("所属设备(序列号)").toString();
        Device device = deviceService.findBySerialNo(serialNo);
        if (device == null) {
            throw new CustomRunTimeException("rowNum:" + (rowNum + 1) + ","
                    + MessageFormat.format(i18nComponent.getMessage("device.notExist.withSN"), serialNo));
        }

        String name = cells.get("物料名称").toString();
        if (materielDao.exists(QMateriel.materiel.name.eq(name).and(QMateriel.materiel.device.serialNo.eq(serialNo)))) {
            throw new CustomRunTimeException("rowNum:" + (rowNum + 1) + ","
                    + MessageFormat.format(i18nComponent.getMessage("materiel.exist"), name, serialNo));
        }

        int checkYear = Integer.parseInt(cells.get("检测周期-年").toString());
        int checkMonth = Integer.parseInt(cells.get("检测周期-月").toString());
        int checkDay = Integer.parseInt(cells.get("检测周期-日").toString());
        int remindYear = Integer.parseInt(cells.get("预警周期-年").toString());
        int remindMonth = Integer.parseInt(cells.get("预警周期-月").toString());
        int remindDay = Integer.parseInt(cells.get("预警周期-日").toString());

        if (checkYear != 0 || checkMonth != 0 || checkDay != 0 || remindYear != 0 || remindMonth != 0
                || remindDay != 0) {
            int checkTotal = checkYear * 365 + checkMonth * 30 + checkDay;
            int remindTotal = remindYear * 365 + remindMonth * 30 + remindDay;
            if (checkTotal < remindTotal) {
                throw new CustomRunTimeException("rowNum:" + (rowNum + 1) + ","
                        + i18nComponent.getMessage("materiel.remindCannotGreaterThanCheck"));
            }
        }

        String number = cells.get("物料编码").toString();
        String beginTime = cells.get("投运时间").toString();
        Date beginDate = DateFormatUtil.getDateByParttern(beginTime, "yyyy-MM-dd");

        Materiel materiel = new Materiel(number, name, device, beginDate, checkYear, checkMonth, checkDay, remindYear,
                remindMonth, remindDay);

        return materiel;
    }

}
