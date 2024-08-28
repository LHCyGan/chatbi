import {listMyOrderByPageUsingPOST, listOrderByPageUsingPOST} from '@/services/ShierBI/aiFrequencyOrderController';
import { payCodeUsingPOST, tradeQueryUsingPOST } from '@/services/ShierBI/aliPayController';
import { ProColumns } from '@ant-design/pro-components';
import { Button, Image, message, Table, Tag } from 'antd';
import Search from 'antd/es/input/Search';
import Modal from 'antd/es/modal/Modal';
import moment from 'moment';
import React, { useEffect, useState } from 'react';

const MyOrder: React.FC = () => {
  const [er, setEr] = useState<string>();
  const [alipayNumber, setAlipayNumber] = useState<string>();
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);

  /**
   * 初始值
   */
  const initSearchParams = {
    current: 1,
    pageSize: 10,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  /**
   * 查询参数
   */
  const [searchParams, setSearchParams] = useState<API.AiFrequencyOrderQueryRequest>({
    ...initSearchParams,
  });

  const [loading, setLoading] = useState<boolean>(true);
  const [orderTotal, setOrderTotal] = useState<number>(0);
  const [orderList, setOrderList] = useState<API.AiFrequencyOrderVO[]>();

  const data = orderList;

  /**
   * 设置订单状态文字显示
   * @param value
   */
  const renderStatus = (value: number) => {
    let color = 'default';
    let status = '';
    if (value === 0) {
      color = 'blue';
      status = '待支付';
    }
    if (value === 1) {
      color = 'success';
      status = '支付完成';
    }
    if (value === 2) {
      color = 'error';
      status = '超时订单';
    }
    if (value === 3) {
      color = 'gold';
      status = '订单已取消';
    }
    return <Tag color={color}>{status}</Tag>;
  };

  const columns: ProColumns<API.AiFrequencyOrderVO>[] = [
    {
      title: '订单号',
      width: 300,
      dataIndex: 'id',
      key: 'name',
      fixed: 'left',
      align: 'center',
      copyable: true,
    },
    {
      title: '购买次数',
      width: 150,
      dataIndex: 'purchaseQuantity',
      key: 'purchaseQuantity',
      align: 'center',
    },
    { title: '单价', width: 150, dataIndex: 'price', key: 'price', align: 'center' },
    { title: '总价', width: 150, dataIndex: 'totalAmount', key: 'totalAmount', align: 'center' },
    {
      title: '订单状态',
      dataIndex: 'orderStatus',
      // 枚举
      // @ts-ignore
      render: renderStatus,
      key: 'orderStatus',
      align: 'center',
      width: '250px',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      render: (text) => moment(text).format('YYYY-MM-DD HH:mm:ss'),
      align: 'center',
      width: '250px',
    },
    {
      title: '操作',
      key: 'operation',
      fixed: 'right',
      render: (value, record, index) => (
        <>
          {record.orderStatus === 1 ? (
            <Button type="primary" disabled style={{color:"green"}}>
              已支付
            </Button>
          )  : record.orderStatus === 2 ? (
            <Button type="primary" disabled style={{color:'red'}}>
              订单超时
            </Button>
          ) : record.orderStatus === 3 ? (
            <Button type="primary"  disabled style={{color:'gold'}}>
              订单已取消
            </Button>
          ) : (
            <Button type="primary" onClick={() => orderInfo(record.id)}>
              付款
            </Button>
          )}
        </>
      ),
      align: 'center',
      width: '150px',
    },
  ];

  /**
   * 获取订单列表
   * @param id
   */
  const orderInfo = async (id: string) => {
    setOpen(true);
    // @ts-ignore
    const res = await payCodeUsingPOST({ orderId: id });
    console.log('订单流水号信息：', res.data?.alipayAccountNo);
    setEr(res?.data?.qrCode);
    // 获取查询查询的流水号
    setAlipayNumber(res.data?.alipayAccountNo);
  };

  /**
   * 获取所有用户订单列表
   */
  const loaData = async () => {
    const res = await listOrderByPageUsingPOST(searchParams);
    console.log('订单列表：', res?.data);
    if (res.code === 0) {
      setOrderList(res?.data?.records);
      // @ts-ignore
      setOrderTotal(res?.data?.total);
    } else {
      message.error('获取失败');
    }
    setLoading(false);
  };

  useEffect(() => {
    loaData();
  }, [searchParams]);

  /**
   * 确定并查询结果
   */
  const okAndSearch = async (alipayNumber: any) => {
    await tradeQueryUsingPOST({ alipayAccountNo: alipayNumber });
  };
  const handleOk = () => {
    // 传递alipayNumber作为参数
    // @ts-ignore
    okAndSearch(alipayNumber);
    message.success('已成功支付');
    setConfirmLoading(true);
    setTimeout(() => {
      setOpen(false);
      setConfirmLoading(false);
    }, 1000);
    location.reload();
    loaData();
  };

  const handleCancel = () => {
    console.log('取消支付');
    location.reload();
    setOpen(false);
  };

  return (
    <>
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <div style={{ marginTop: '50px', width: '1024px', textAlign: 'center' }}>
          <Search
            placeholder="请输入要支付的订单号"
            loading={loading}
            enterButton
            onSearch={(value: any) => {
              setSearchParams({
                ...initSearchParams,
                id: value,
              });
            }}
          />
        </div>
      </div>
      <div style={{ textAlign: 'center', marginTop: '10px', color: 'blue' }}>
        请根据对应的订单号付款
      </div>
      <div className="my-order" style={{ margin: '50px' }}>
        <Table
          bordered
          columns={columns}
          dataSource={data}
          style={{ textAlign: 'center' }}
          pagination={{
            // 设置分页
            showTotal: () => `共 ${orderTotal} 条记录`,
            showSizeChanger: true,
            showQuickJumper: true,
            pageSizeOptions: ['10', '20', '30'],
            onChange: (page, pageSize) => {
              setSearchParams({
                ...searchParams,
                current: page,
                pageSize,
              });
            },
            current: searchParams.current,
            pageSize: searchParams.pageSize,
            total: orderTotal,
            position: ['bottomCenter'],
          }}
        />
        <Modal
          title="请使用支付宝扫码付款"
          open={open}
          onOk={handleOk}
          confirmLoading={confirmLoading}
          onCancel={handleCancel}
        >
          <Image width={200} src={er}></Image>
        </Modal>
      </div>
    </>
  );
};
export default MyOrder;
