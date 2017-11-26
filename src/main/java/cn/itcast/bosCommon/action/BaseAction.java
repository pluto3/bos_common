package cn.itcast.bosCommon.action;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Results({ @Result(name = BaseAction.JSON, type = BaseAction.JSON) })
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	/**
	 * 说明：action重构类
	 * @author wangkai
	 * @time：2017年11月2日 下午3:16:54
	 */
	// ====================定义的常量
	private static final long serialVersionUID = 1L;
	public static final String JSON = "json";
	public static final String REDIRECT = "redirect";
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	// 默认构造器
	public BaseAction() {
		// 获得当前类型的带有泛型类型的父类
		ParameterizedType ptClass = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 获得运行期的泛型类型
		@SuppressWarnings("unchecked")
		Class<T> modelClass = (Class<T>) ptClass.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// ==================分页
	// 属性驱动获取分页参数
	public int page;
	public int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	//属性驱动获取多个id字符串
	public String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}

	// ==================将分页结果数据重新组装并压入root栈顶
	public void pushPageDataToValustackRoot(Page<T> pageResponse) {
		HashMap<String, Object> resultMap = new HashMap<>();
		// 总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		// 当页数据列表
		resultMap.put("rows", pageResponse.getContent());
		// 要转换为json的java对象压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
	}

}
