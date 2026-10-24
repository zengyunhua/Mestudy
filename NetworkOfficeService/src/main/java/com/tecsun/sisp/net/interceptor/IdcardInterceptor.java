package com.tecsun.sisp.net.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tecsun.sisp.net.modules.common.XZEnum;
import com.tecsun.sisp.net.modules.service.business.QueryService;

/**
 * 需要通过身份证号查询出个人编号的业务,用此拦截器统一转换
 * <p>因为query业务,基本上都是需要通过身份证号码查询个人编号的,为了号管理,和精简代码,将查询的逻辑放到拦截器中,好统一管理
 * <p>该拦截器只会拦截 /net/query/idcard/xz/**开头的,如有需要,请在springmvc配置文件中添加拦截器拦截地址
 * 
 *
 */
public class IdcardInterceptor implements HandlerInterceptor{
	
	@Autowired
	private QueryService queryService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从链接中拿到险种类型
		String uri = request.getRequestURI();
		String xzKey = "";
		
		String xz = uri.split("/xz/")[1].split("/")[0];
		//从险种枚举中获取对应的险种key
		XZEnum XZ = null;
		try{
			XZ = XZEnum.valueOf(xz);
		} catch (Exception e){
			//证明转换不了,没有此险种,
		}
		
		if(XZ==null){
			//提示险种错误
			request.getRequestDispatcher("/net/query/noXZ").forward(request, response);
			return false;
		}else{
			xzKey = XZ.getKey();
					
		}
			
		//从参数中拿到身份证号
		String idcard = request.getParameter("idcard");
		
		if(StringUtils.isEmpty(idcard)){
			//提示身份证号不可为空
			request.getRequestDispatcher("/net/query/mustHaveIdCard").forward(request, response);
			return false;
		}else{
			List<String> grbhList = queryService.getGrbh(idcard,xzKey);
			if(grbhList==null || grbhList.isEmpty()){
				//跳转 转发请求
				request.getRequestDispatcher("/net/query/noGrbh").forward(request, response);
				return false;
			}else if(grbhList.size()>1){
				//跳转 转发请求
				request.getRequestDispatcher("/net/query/multiGrbh").forward(request, response);
				return false;
			}else{
				//将个人编号设置到请求中
				request.setAttribute("grbh", grbhList.get(0));
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
