import React, { Component, Fragment } from 'react';
import { Button, Card, Col, Drawer, Row, Select, Table, Tabs, Tag, Tooltip, Icon } from "antd";
import { connect } from "react-redux";
import * as actionCreators from "../actions/actions.js"
import BsDetail from "./BsDetail";
import BsBadTrx from "./BsBadTrx";



const { TabPane } = Tabs;
const { Option } = Select;


class BsContent extends Component {

    state = {
        visible: false,
        idprofile: 0
    }

    componentDidMount() {
        const transaction = {
            rangeDate: null,
            decision: null,
            debtorAccount: null,
            debtorName: null,
            debtorBic: null,
            debtorCountry: null,

            creditorAccount: null,
            creditorName: null,
            creditorBic: null,
            creditorCountry: null,

            amountInf: null,
            amountSup: null,
            transferType: null,

        }
        this.props.fetchAllTransfers(0, 10);
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        return true;
    }

    handlePagination = page => {
        if (this.props.transaction === undefined) {
            this.props.fetchAllTransfers((page - 1) * 10, 10);
        }
        else {
            this.props.searchTransfer(this.props.transaction, (page - 1) * 10, 10);
        }
        console.log(page);
    };

    handlePaginationSearchProfile = page => {
        console.log(page);
        this.props.loadTransactionsOfSearchProfiles(this.state.idprofile, (page - 1) * 10, 10);
    }

    displayDetail = (key, e) => {
        e.preventDefault();
        this.props.fetchTransfer(key);
        this.showDrawer();
    }

    displayError = (text, record) => {
        if (text === "Y") {
            return <Tooltip placement="leftBottom" title={record.message}>ERROR</Tooltip>
        } else {
            return <Tooltip placement="leftBottom" title={record.message}>SUCCESS</Tooltip>
        }

    }

    callback = key => {

        if (key === '1') {
            this.props.fetchAllTransfers(0, 10);
        }
        if (key === '2') {
            this.props.fetchSearchProfiles();
        }
        if (key === '3') {
            this.props.fetchBadTransfers(0, 10);
        }
    }

    showDrawer = () => {
        this.setState({
            visible: true,
        });
    };

    onClose = () => {
        this.setState({
            visible: false,
        });
    };

    handleChangeSearchProfile = idprofile => {
        this.setState({ idprofile: idprofile });
        this.props.loadTransactionsOfSearchProfiles(idprofile, 0, 10);
    }

    render() {
        const listsearchprofiles = this.props.listsearchprofiles;
        const propstable = {
            bordered: false,
            loading: false,
            pagination: { onChange: this.handlePagination, position: "bottom", total: this.props.transfersCount },
            title: undefined,
            showHeader: true,
        };

        const propstablesearchprofile = {
            bordered: false,
            loading: false,
            pagination: { onChange: this.handlePaginationSearchProfile, position: "bottom", total: this.props.transfersCountOfSearchProfile },
            title: undefined,
            showHeader: true,
        };


        const columns = [
            {
                title: "Id",
                dataIndex: "transferId",
                key: "transferId",
                render: (text, record) => (
                    <Icon type="search" style={{fontSize: '14px'}} onClick={(e) => {this.displayDetail(record.transferId, e)}}/>
                    // <Button type="link" onClick={(e) => {
                    //     this.displayDetail(record.transferId, e)
                    // }}>{text}</Button>
                )
            },
            {
                title: "Type",
                dataIndex: "transferType",
                key: "transferType",
                render: (text, record) => (
                    <span style={{ fontSize: '12px' }} onClick={(e) => {
                        this.displayDetail(record.transferId, e)
                    }}>{text}</span>
                )
            },
            {
                title: "Debtor",
                dataIndex: "debtorAccount",
                key: "debtorAccount",
                render: (text) => (
                    <span style={{ fontSize: '12px' }}>{text}</span>
                )
            },
            {
                title: "Creditor",
                dataIndex: "creditorAccount",
                key: "creditorAccount",
                render: (text) => (
                    <span style={{ fontSize: '12px' }}>{text}</span>
                )
            },
            {
                title: "Amount",
                dataIndex: "amount",
                key: "amount",
                render: (text) => (
                    <span style={{ fontSize: '12px' }}>{text}</span>
                )
            },
            {
                title: "Currency",
                dataIndex: "currency",
                key: "currency",
                render: (text) => (
                    <span style={{ fontSize: '12px' }}>{text}</span>
                )
            },
            {
                title: "Alert",
                dataIndex: "decision",
                key: "decision",
                filters: [
                    { text: "Pass", value: "PASS" },
                    { text: "Blocked", value: "BLOCKED" }
                ],
                onFilter: (value, record) => {
                    return record.decision === value;
                },
                render: (decision) => (
                    <span>
                        {
                            <Tag color={(decision === 'PASS') ? 'green' : 'volcano'}>{decision}</Tag>
                        }
                    </span>
                )
            },
            {
                title: "Duration(ms)",
                dataIndex: "duration",
                key: "duration",
                render: (text) => (
                    <span style={{ fontSize: '12px' }}>{text}</span>
                )
            },
            {
                title: "Status",
                dataIndex: "errorcompute",
                key: "errorcompute",
                filters: [
                    { text: "OK", value: "N" },
                    { text: "KO", value: "Y" }
                ],
                onFilter: (value, record) => {
                    return record.errorcompute === value;
                },
                render: (text, record) => (
                    <span>
                        {
                            //<Tag color={(text === "N") ? 'green' : 'volcano'}>{this.displayError(text, record)}</Tag>
                            <center>
                                <Icon
                                    style={{ fontSize: '24px', color: (text === "N") ? 'green' : 'red' }}
                                    type={(text === "N") ? 'like' : 'dislike'}
                                />
                            </center>
                        }
                    </span>
                )
            }
        ];

        return (
            <Card style={{ background: '#fff' }}>
                <Drawer
                    width={1280}
                    onClose={this.onClose}
                    visible={this.state.visible}
                    bodyStyle={{ paddingBottom: 80 }}
                ><BsDetail /></Drawer>
                <Row gutter={8}>
                    <Col span={24}>
                        <Tabs onChange={this.callback} type="card">
                            <TabPane tab="Alerts" key="1">
                                <Table {...propstable} columns={columns} dataSource={this.props.transfers} className="table-striped-rows"/>
                            </TabPane>
                            <TabPane tab="Search Profile" key="2">
                                <Card size="small">
                                    <Row gutter={16}>
                                        <Col span={24}>
                                            <Select id="searchprofile" defaultValue="" style={{ width: 360 }} onChange={this.handleChangeSearchProfile}>
                                                <Option value="">Select Profile</Option>
                                                {listsearchprofiles.map(x => <option key={x.id}>{x.name}</option>)}
                                            </Select>
                                        </Col>
                                    </Row>
                                    <Row gutter={16} style={{ marginTop: '20px' }}>
                                        <Col span={24}>
                                            <Table {...propstablesearchprofile} columns={columns} dataSource={this.props.transfersOfSearchProfile} className="table-striped-rows"/>
                                        </Col>
                                    </Row>
                                </Card>
                            </TabPane>
                            <TabPane tab="Rejected" key="3">
                                <Card size="small">
                                    <BsBadTrx />
                                </Card>
                            </TabPane>
                        </Tabs>
                    </Col>
                </Row>
            </Card>
        );
    }
}

const mapStateToProps = (state) => {
    return state
};
export default connect(mapStateToProps, actionCreators)(BsContent);