package com.tecsun.sisp.npm.service;

import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Guards the HTTP method of the upload resource.
 *
 * <p>{@code ServletFileUpload.isMultipartContent} accepts only POST requests, so a GET
 * mapping compiles and deploys but can never receive a file. This test needs no
 * container: it reads the JAX-RS annotations directly.
 */
public class PolicyApplicationMappingTest {

    @Test
    public void uploadfileIsMappedToPost() throws Exception {
        Method upload = PolicyApplication.class.getMethod("uploadfile", HttpServletResponse.class);

        assertTrue("uploadfile must accept POST", upload.isAnnotationPresent(POST.class));
        assertFalse("uploadfile must not be mapped to GET", upload.isAnnotationPresent(GET.class));
    }
}
