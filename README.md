# Certificate identification system based on big data analysis and Microservice architecture

基于大数据分析的证件认领系统（微服务架构）

  本项目通过面向对象的分析方法，结合证件丢失后招领、认领的实际需求，遵循 MVC的设计模式与分层的设计思想，对基于微服务架构的证件认领系统的架构、业务、功能、数据库以及页面进行了细致的分析与设计，然后根据分析和设计的产物，应用基于 J2EE 的web开发技术以及Spring Boot等微服务框架来构建微服务架构，同时应用Hadoop框架与K-Means 聚类算法来进行大数据存储与分析，完成实际的开发、测试等一系列的系统实现过程。
  此外，系统在多个业务场景下采取了特定的归还措施，既能保证平台业务过程的安全性，也能在一定程度上提高用户主动归还证件的积极性。

1.系统架构如下所示：

![拾遗-系统架构图2.0](
     https://raw.githubusercontent.com/zm12397/hello-world/master/%E6%8B%BE%E9%81%97-%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE2.0.jpg
     )

2.数据可视化：

下图为使用K-mean聚类算法，结合MapReduce计算框架得出的聚类结果，在Map上可以看到国内的聚类中心： ![数据中心]( https://raw.githubusercontent.com/zm12397/hello-world/master/%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83.png)

 下图为使用MapReduce计算框架对丢失地点文本分析后得到的地点信息：

![数据可视化]( https://raw.githubusercontent.com/zm12397/hello-world/master/%E6%95%B0%E6%8D%AE%E5%8F%AF%E8%A7%86%E5%8C%96.png) 
