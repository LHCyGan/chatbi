/**
 * 全局变量
 */

/**
 * 项目logo
 */
import loginImage from '../../public/loginImage.jpg';
import logo from '../../public/logo.svg';
import registerImage from '../../public/registerImage.jpg';

export const IMAGES = [
  import('../../public/catImage/image1.jpg'),
  import('../../public/catImage/image2.jpg'),
  import('../../public/catImage/image3.jpg'),
  import('../../public/catImage/image4.jpg'),
  import('../../public/catImage/image5.jpg'),
  import('../../public/catImage/image6.jpg'),
  import('../../public/catImage/1.jpg'),
  import('../../public/catImage/2.jpg'),
  import('../../public/catImage/3.jpg'),
  import('../../public/catImage/4.jpg'),
  import('../../public/catImage/5.jpg'),
  import('../../public/catImage/6.jpg'),
  import('../../public/catImage/7.jpg'),
  import('../../public/catImage/8.jpg'),
  import('../../public/catImage/9.jpg'),
  import('../../public/catImage/10.jpg'),
];

export const SYSTEM_LOGO = logo;

export const LOGIN_BACKGROUND_IMAGE = loginImage;

export const REGISTER_BACKGROUND_IMAGE = registerImage;


export const WELCOME = 'https://xiaohuolong.blog.csdn.net';

export const CSDN_LINK = 'https://xiaohuolong.blog.csdn.net';

export const CHART_TYPE_SELECT = [
  { value: '折线图', label: '折线图' },
  { value: '柱状图', label: '柱状图' },
  { value: '雷达图', label: '雷达图' },
  { value: '条形图', label: '条形图' },
  { value: '散点图', label: '散点图' },
  { value: '正负条形图', label: '正负条形图' },
  { value: '柱状图框选', label: '柱状图框选' },
  {
    value: 'divider',
    label: '-----------------------多列数据建议选择如下的图表类型-----------------------',
    disabled: true,
  },
  { value: '饼图', label: '饼图' },
  { value: '树图', label: '树图' },
  { value: '热力图', label: '热力图' },
  { value: '漏斗图', label: '漏斗图' },
  { value: '区域图', label: '区域图' },
  { value: '堆叠条形图', label: '堆叠条形图' },
  { value: '玫瑰图', label: '玫瑰图' },
];

/**
 * 默认头像
 */
export const DEFAULT_AVATAR_URL =
  'https://pic.code-nav.cn/yucongming_user_avatar/1659387762899537922/w8rWOgeW-R.jpg';


export const selectUserStatus = [
  { value: 0, label: '正常' },
  { value: 1, label: '注销' },
];
export const selectUserRole = [
  { value: 'user', label: '普通用户' },
  { value: 'admin', label: '管理员' },
  { value: 'ban', label: '封号' },
];
export const selectAvatarUrl = [
  {
    value:
      'https://pic.code-nav.cn/yucongming_user_avatar/1659387762899537922/w8rWOgeW-R.jpg',
    label: '默认头像',
  },
  {
    value:
      'https://pic.code-nav.cn/yucongming_midjourney_image/1696156156386410497/_r0_c1f6b-b6e83603a609.jpg',
    label: '动漫美女',
  },
  {
    value:
      'https://pic.code-nav.cn/yucongming_midjourney_image/1696156156386410497/_r0_c0942-14014106d4a6.jpg',
    label: '御姐1号',
  },
  {
    value:
      'https://pic.code-nav.cn/yucongming_midjourney_image/1696158981455675394/_r0_c0bc5-08bc98de30ee.jpg',
    label: '帅气小男孩',
  },
 
];
