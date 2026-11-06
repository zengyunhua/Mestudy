package com.tecsun.sisp.iface.server.model.service.employment.impl;

import com.tecsun.sisp.iface.common.vo.employment.param.job.SavePictureVo;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JobServicePictureNameTest {

    private static String nameFor(String zjhm) {
        SavePictureVo vo = new SavePictureVo();
        vo.setZjhm(zjhm);
        return JobServiceImpl.constructPicName(vo);
    }

    @Test
    public void acceptsIdNumbers() {
        assertTrue(nameFor("11010119900101001X").matches("11010119900101001X_\\d{14}\\.jpg"));
        assertTrue(nameFor(" 110101199001010015 ").startsWith("110101199001010015_"));
    }

    @Test
    public void rejectsPathsAndBlanks() {
        assertNull(nameFor("../../../opt/tomcat/webapps/ROOT/shell"));
        assertNull(nameFor("a\\b"));
        assertNull(nameFor(""));
        assertNull(nameFor(null));
    }
}
