
package com.goodcol.template.expr.ast;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.goodcol.template.stat.Scope;

/**
 * Map
 * 
 * 1：定义 map 常量
 *   {k1:123, k2:"abc", 'k3':true, "k4":[1,2,3], k5:1+2}
 *   如上所示，map定义的 key 可以为 String 或者 id 标识符，而右侧的 value 可以是任意的常量与表达式
 * 
 * 2：取值
 *   先将 Map 常量赋值给某个变量： #set(map = {...})
 *   map['k1']
 *   map["k1"]
 *   map[expr]
 *   map.get("k1")
 *   map.k1
 *   
 *   如上所示，当以下标方式取值时，下标参数可以是 string 与  expr，而 expr 求值以后的值必须也为 string类型
 *   当用 map.k1 这类 field 字段取值形式时，则是使用 id 标识符，而不是 string 形参数
 *   
 *   注意：即便是定义的时候 key 用的是 id 标识符，但在取值时也要用 string 类型下标参数或 expr 求值后为 string
 *        定义时 key 可以使用 id 标识符是为了书写方便，本质上仍然是 string
 * 
 * 3：可创建空 map，如： #(map = {})
 */
public class Map extends Expr {
	
	private LinkedHashMap<Object, Expr> map;
	
	public Map(LinkedHashMap<Object, Expr> map) {
		this.map = map;
	}
	
	public Object eval(Scope scope) {
		LinkedHashMap<Object, Object> valueMap = new LinkedHashMap<Object, Object>(map.size());
		for (Entry<Object, Expr> e : map.entrySet()) {
			valueMap.put(e.getKey(), e.getValue().eval(scope));
		}
		return valueMap;
	}
}






