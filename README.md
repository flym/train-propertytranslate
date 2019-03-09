### 通用性属性转换和数据渲染

本示例详细思想及介绍: [https://www.iflym.com/index.php/code/201903080001.html](https://www.iflym.com/index.php/code/201903080001.html)

本示例主要描述使用了通用的转换器来转换一定数据格式的对象，通过调用链来完成多次的处理，最终将数据转换成符合业务要求的数据格式。

整个过程全部由配置文件来完成（示例中使用了xml来描述），包括数据源格式，转换器链，以及数据输出模板，都通过配置来完成。通过数据表存储，
使得配置信息可以随时进行调整，以达到线上不需要重新发布就可以调整业务场景的需要。

本工程使用kotlin完成~