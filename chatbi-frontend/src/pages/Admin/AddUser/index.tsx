import Footer from '@/components/Footer';
import {
  selectAvatarUrl,
  // selectGender,
  selectUserRole,
  // selectUserStatus,
  SYSTEM_LOGO,
  WELCOME,
} from '@/constants';
import { addUserUsingPost, getLoginUserUsingGet} from '@/services/chatbi/userController';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import {LoginForm, ProForm, ProFormText} from '@ant-design/pro-components';
import { ProFormSelect } from '@ant-design/pro-form/lib';
import { useEmotionCss } from '@ant-design/use-emotion-css';
import { Helmet, useModel } from '@umijs/max';
import { message, Tabs } from 'antd';
import React from 'react';
import { flushSync } from 'react-dom';
import Settings from '../../../../config/defaultSettings';


const Login: React.FC = () => {
  const { setInitialState } = useModel('@@initialState');
  const containerClassName = useEmotionCss(() => {
    return {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    };
  });
  const fetchUserInfo = async () => {
    const userInfo = await getLoginUserUsingGet();
    if (userInfo) {
      flushSync(() => {
        setInitialState((s): any => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };
  const handleSubmit = async (values: API.UserLoginRequest) => {
    try {
      // 注册
      const res = await addUserUsingPost(values);
      if (res.code === 0) {
        const defaultLoginSuccessMessage = '新增用户成功！';
        message.success(defaultLoginSuccessMessage);
        await fetchUserInfo();
        location.reload();
        return;
      } else {
        message.error(res.message);
      }
    } catch (error) {
      const defaultLoginFailureMessage = '新增用户失败，请重试！';
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };
  return (
    <div className={containerClassName}>
      <Helmet>
        {'新增用户'}- {Settings.title}
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="讯飞星火智能BI"
          subTitle={
            <a href={WELCOME} target="_blank">
              讯飞星火智能BI
            </a>
          }
          submitter={{
            searchConfig: {
              submitText: '添加新用户',
            },
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.UserLoginRequest);
          }}
        >
          <Tabs centered activeKey={'account'}>
            <Tabs.TabPane key={'account'} tab={'新增用户信息填写'} />
          </Tabs>
          {
            <>
              <ProFormText
                name="userName"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={'prefixIcon'} />,
                }}
                placeholder="用户名"
                rules={[
                  {
                    required: true,
                    message: '用户名不能为空!',
                  },
                ]}
              />

              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={'prefixIcon'} />,
                }}
                placeholder="用户账户 "
                rules={[
                  {
                    required: true,
                    message: '用户账户不能为空!',
                  },
                  {
                    min: 4,
                    message: '用户账户长度不小于4位',
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={'prefixIcon'} />,
                }}
                placeholder={'密码'}
                rules={[
                  {
                    required: true,
                    message: '请输入密码！',
                  },
                  {
                    min: 8,
                    message: '密码长度不得小于8',
                  },
                ]}
              />
              <ProFormSelect
                name="userAvatar"
                fieldProps={{
                  size: 'large',
                }}
                label="用户头像"
                options={selectAvatarUrl}
                placeholder={'请选择用户头像 '}
                rules={[
                  {
                    required: true,
                    message: '请输入选择用户头像!',
                  },
                ]}
              />
              <ProFormSelect
                name="userRole"
                fieldProps={{
                  size: 'large',
                }}
                label="用户角色"
                options={selectUserRole}
                placeholder={'选择用户角色'}
                rules={[
                  {
                    required: true,
                    message: '请选择用户角色',
                  },
                ]}
              />



            </>
          }
          <div
            style={{
              marginBlockEnd: 24,
            }}
          />
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Login;
