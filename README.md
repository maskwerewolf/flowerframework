##
```
根据datasource + table + conditions  + result 封装Jdbc
Jdbc.build(datasource,tableName)
默认datasource , 获取spring容器中 datasource
默认tableName, 获取 @table中注解
```
## 1、插入数据
```
   Jdbc.build().insert(obj)
```
## 2、修改数据
```
   Jdbc.build().update(class).where("column = ? ",value).set("column", value)

   Jdbc.build().update(class).where("c1=? and c2=? ",c1 ,c2).set(new HashMap(){
         put("...","....");
         put("...","....");
   })

```
## 3 INC操作
```
   Jdbc.build().update(class).where("......").inc("c1",v1).inc("c1",v2).set()
   Jdbc.build().update(class).where("......").inc("c1",v1).inc("c1",v2).set(....)
```

## 查询

```
    Jdbc.build().query(clsss,colums ...).where(....).page(limit,page);
    Jdbc.build().query(class).where().order("column desc ").first();
    Query query =  Jdbc.build().query(Object.class).where("");
       if(conditions){
           query.where(....)
       }
      query.where(....).all()
```

## SQL
```
  Jdbc.build().SQL().findList("sql",class );
```
