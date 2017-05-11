##
```
根据datasource + table + conditions  + result 封装Jdbc
Jdbc.build(datasource,tableName)
默认datasource , 获取spring容器中 datasource
默认tableName, 获取 @table中注解
```
##1、插入数据
```
   Jdbc.build().insert(userLevel)
```
##2、修改数据
```
   Jdbc.build().update(class).where("column = ? ",value).set("column", value)

      Jdbc.build().update(class).where("column = ? ",value).set(new HashMap(){
         put("...","....");
         put("...","....");
      })
```
