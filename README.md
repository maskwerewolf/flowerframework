# flower-jdbc2.0


>maven

    <dependency>
        <groupId>com.github.maskwerewolf</groupId>
        <artifactId>flower-mysql</artifactId>
        <version>2.0</version>
    </dependency>
    
    
##insert

     Number insert(Object object) 
     
     int insertBatch(List<?> objectList)
     
      
##update
    
    
         int update(Query query, Update update, Class<?> entityClass) 
         
         int update(Query query, Update update, String tableName) 
    
##select

     <T> List<T> find(Query query, Class<T> entityClass) 
     <T> List<T> find(Select select, Query query, Class<T> entityClass)
     List<Map<String, Object>> findForMaps(Query query, Class<?> entityClass) 
     List<Map<String, Object>> findForMaps(Select select, Query query, Class<?> entityClass)
     <T> T findOne(Query query, Class<T> entityClass)
     Map<String, Object> findForMap(Query query, Class<?> entityClass) 
     Map<String, Object> findForMap(Query query, String tableName) 
     <T> T findOne(Select select, Query query, Class<T> entityClass) 
     
##count

         long count(Query query, String tableName) 
         long count(Query query, Class<?> entityClass) 

##delete


     int delete(Query query, Class<?> entityClass)
      
     int delete(Query query, String tableName) 
