package com.tecsun.sisp.net.modules.controller.business;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsun.sisp.net.common.Constants;
import com.tecsun.sisp.net.common.Result;
import com.tecsun.sisp.net.modules.BaseController;
import com.tecsun.sisp.net.modules.common.XZEnum;
import com.tecsun.sisp.net.modules.entity.business.QueryVo;
import com.tecsun.sisp.net.modules.service.business.QueryService;

/**
 * 查询类业务controller
 * <p><b>如果需要通过险种类型查询出个人编号的,链接形式为/idcard/xz/....</b>
 * <p>实在是不知道业务的具体情况,,,先这样设计着.
 * @since 2018/06/26
 */

@RestController
@RequestMapping("/net/query")
public class QueryController extends BaseController{

	@Autowired
	private QueryService queryService;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 提示信息 -必须传入身份证号查询
	 * @return
	 */
    @RequestMapping("/mustHaveIdCard")
    public Result mustHaveIdCard(){
    	return new Result("400","必须传入身份证号!",null);
    }
    /**
	 * 提示信息 没有查询到个人编号
	 * @return
	 */
    @RequestMapping("/noGrbh")
    public Result noGrbh(){
    	return new Result("404","没有查询到个人编号,请检查身份证号码是否正确!",null);
    }
    /**
	 * 提示信息 查询到多个个人人编号
	 * @return
	 */
    @RequestMapping("/multiGrbh")
    public Result multiGrbh(){
    	return new Result("302","查询的险种有多个账户,请合并!",null);
    }
    
    /**
	 * 提示信息 地址错误
	 * @return
	 */
    @RequestMapping("/noUrl")
    public Result noUrl(){
    	return new Result("402","地址错误!",null);
    }
    
    
    /**
	 * 提示信息 险种错误
	 * @return
	 */
    @RequestMapping("/noXZ")
    public Result noXZ(){
    	return new Result("406","险种错误!",null);
    }
	
	/**
	 * 个人实缴记录(养老,等等)  如果查询有问题,请见擦汗sql是否正确
	 * <p>说明:{xz}是动态参数,可选值有 XZEnum 枚举里面的几项,如果是查养老保险,则前端人员的请求地址则为 .../idcard/xz/xz11/grsjjl
	 * <p>详情请 见 XZEnum
	 * <p>此设计是为了以后如果新增了其他险种的个人实缴记录,这个接口可以复用,后端开发只需要在service层中做逻辑判断,根据不同的险种调用不同的dao接口,返回map
	 * @see XZEnum
	 * @param idcard
	 * @return
	 */
	@RequestMapping(value="/idcard/xz/{xz}/grsjjl",method=RequestMethod.GET)
	public Result getGrsjjl(String idcard,@PathVariable("xz") String xz,String nd){
		//从request中拿到个人编号
		String grbh = (String) request.getAttribute("grbh");
		QueryVo qv = new QueryVo(grbh,xz,nd,0,0);//不分页
		Map<String,Object> map = queryService.getGrsjjl(qv);
		return new Result(Constants.RESULT_MESSAGE_SUCCESS,"success",map);
		
	}
	
	/**
	 * 个人参保证明查询
	 * 此逻辑还没走通
	 * @param idcard
	 * @return
	 */
	@RequestMapping(value="/idcard/grcbzm",method=RequestMethod.GET)
	public Result getGrcbzm(String idcard){
		//从request中拿到个人编号
		List<Map<String,Object>> map = queryService.getGrcbzm(idcard);
		return new Result(Constants.RESULT_MESSAGE_SUCCESS,"success",map);
	}
	
	/**
	 * 个人养老保险年账户信息查询 
	 * <p>说明:{xz}是动态参数,可选值有 XZEnum 枚举里面的几项,如果是查养老保险,则前端人员的请求地址则为 .../idcard/xz/xz11/grsjjl
	 * <p>详情请 见 XZEnum
	 * <p>此设计是为了以后如果新增了其他险种的年账户信息查询,这个接口可以复用,后端开发只需要在service层中做逻辑判断,根据不同的险种调用不同的dao接口,返回map
	 * @see XZEnum
	 * @param idcard
	 * TODO 可以在此加上分页功能,因为可能会有接近好几十条的数据,不一定需要一次性取出来	
	 * @return
	 */
	@RequestMapping(value="/idcard/xz/{xz}/grylbxnzhxx",method=RequestMethod.GET)
	public Result getGrylbxnzhxx(String idcard,String nd,@PathVariable("xz") String xz,
			@RequestParam(value="pageNo",defaultValue="1")Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="20")Integer pageSize){
		//从request中拿到个人编号
		String grbh = (String) request.getAttribute("grbh");
		//将条件封装到查询的包装对象
		QueryVo qv = new QueryVo(grbh,xz,nd,pageNo,pageSize);
		Map<String,Object> map = queryService.getGrylbxnzhxx(qv);
		return new Result(Constants.RESULT_MESSAGE_SUCCESS,"success",map);
	}
	
	/**
	 * 灵活就业人员缴费基数查询
	 * @param idcard
	 * @return
	 */
	@RequestMapping(value="/idcard/lhjyryjfjs",method=RequestMethod.GET)
	public Result getLhjyryjfjs(String idcard){
		QueryVo qv = new QueryVo(idcard,0,0);
		Map<String,Object> map = queryService.getLhjyryjfjs(qv);
		return new Result(Constants.RESULT_MESSAGE_SUCCESS,"success",map);
	}
}
