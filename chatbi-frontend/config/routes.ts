export default [
  {
    path: '/user', layout: false,
    routes: [
      {path: '/user/login', name: '登录', component: './User/Login'},
      {path: '/user/register', name: '注册', component: './User/Register'},
    ]
  },
  { path: '/', redirect: '/add_chart' },
  { path: '/add_chart', name: '智能分析', icon: 'barChart', component: './AddChart' },
  { path: '/add_chart_async', name: '智能分析（异步）', icon: 'barChart', component: './AddChartAsync' },
  { path: '/my_chart', name: '我的图表', icon: 'pieChart', component: './MyChart' },
  { path: '/viewChartData/:id', icon: 'checkCircle', component: './ViewChartData', name: '查看图表', hideInMenu: true,  },
  { path: '/ai_question/assistant', name: '讯飞星火 AI助手', icon: 'barChart',component:'./AiChatAssistant/AddChat'},
  {
    path: '/admin',
    icon: 'crown',
    name: '系统管理',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/sub-page' },
      { path: '/admin/index', name: '管理员身份介绍', component: './Admin' },
      { path: '/admin/user_manage', name: '用户管理', component: './Admin/UserManage' },
      { path: '/admin/adduser', name: '添加用户', component: './Admin/AddUser' },
      { path: '/admin/chart_manage', name: '图表管理', component: './Admin/ChartManage' },
      // { path: '/admin/chat_manage', name: '对话管理', component: './Admin/AiChatManage' },
      // { path: '/admin/user_order_manage', name: '订单管理', component: './Admin/UserOrderManage' },
      // { path: '/admin/user_pay_order_manage', name: '管理员支付订单', component: './Admin/UserPayOrderManage' },
      // { path: '/admin/user_pay_info_manage', name: '支付信息结果查询', component: './Admin/UserPayInfoManage' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
