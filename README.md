#钉钉v5.1.17.10版本文件删除工具
1. 基于Xposed框架，Hook获取用户信息、Conversation
2. 使用Conversation中的SpaceId遍历文件信息，接口listDentry
3. 使用deleteDentry删除相关文件
4. 仅用于方便删除自己相关文件