# flower-jdbc

## 简化JDBC使其更优雅
```
Jdbc.build(datasource,tableName)
默认datasource , 获取spring容器中 datasource
默认tableName, 获取 @table中注解
事务采用spring transactionManager 事务
设置不同、datasource 、 tableName 可以更灵活的进行分表分库操作
```
## Maven
```
<dependency>
  <groupId>com.github.maskwerewolf</groupId>
  <artifactId>flower-mysql</artifactId>
  <version>1.0</version>
</dependency>

<dependency>
  <groupId>com.github.maskwerewolf</groupId>
  <artifactId>flower-core</artifactId>
  <version>1.0</version>
</dependency>

```
## 1、INSERT
```
   Jdbc.build().insert(obj)
   execute sql: insert into (c1,c2)values (obj.v1,obj.v2)
```
## 2、UPDATE
```
   Jdbc.build().update(class).where("c1 = ? ",v1).set("c2", v2)
   execute sql : update table set c1 = v1 where c2 = v2
   Jdbc.build().update(class).where("c1=? and c2=? ",c1 ,c2).set(new HashMap(){
         put("...","....");
         put("...","....");
   })
   execute sql : update table set .... , where c1=c1 ,c2 = c2

```
## 3 INC
```
   Jdbc.build().update(class).where("......").inc("c1",v1).inc("c2",v2).set()
   execute sql : update set c1 = c1+ v1 ,c2 = c2+v2 where .........
   Jdbc.build().update(class).where("......").inc("c1",v1).inc("c1",v2).set(....)
```

## QUERY

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
