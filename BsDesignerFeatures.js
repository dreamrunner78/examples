import React, { Component, Fragment } from 'react';
import {
    Button,
    Card,
    Col,
    Drawer,
    Form,
    Icon,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Row,
    Select,
    Switch,
    Table,
    Tag,
    Tooltip
} from "antd";
import { connect } from "react-redux";
import * as actionCreators from "../actions/actions";
import axios from "axios";
import * as API from "../constants/params";
//import Highlighter from 'react-highlight-words';


const { TextArea } = Input;
const { Option } = Select;

class BsDesignerFeatures extends Component {
    state = {
        id: undefined,
        name: '',
        description: '',
        classname: '',
        transfertype: '',
        enable: false,
        fixed: false,
        value: 0.0,
        computationtype: '',
        featuretype: '',
        rule: '',
        actionAddOrModifyFeature: '',
        visible: false,
        tmpruleContent: '',
        searchText: '',
        searchedColumn: '',
    }

    componentDidMount() {
        // To disabled submit button at the beginning.
        this.props.form.validateFields();
        this.props.fetchAllFeatures();
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        return true;
    }

    handleSwitch = enable => {
        //this.setState({ footer: enable ? footer : undefined });
        console.log(enable);
    };

    displayDetailFeature = (record, e) => {
        e.preventDefault();
        this.props.form.setFieldsValue({
            id: record.id,
            name: record.name,
            description: record.description,
            classname: record.classname,
            transfertype: record.transfertype,
            featuretype: record.featuretype,
            computationtype: record.computationtype,
            enable: (record.enable === 'Y'),
            fixed: (record.fixed === 'Y'),
            value: record.value,
            rule: record.rule,
            tmprule: record.rule
        });
    }

    resetForm = e => {
        this.props.form.resetFields();
    }

    confirmSave = event => {

        const feature = {
            id: this.props.form.getFieldValue("id"),
            name: this.props.form.getFieldValue("name"),
            description: this.props.form.getFieldValue("description"),
            classname: this.props.form.getFieldValue("classname"),
            transfertype: this.props.form.getFieldValue("transfertype"),
            featuretype: this.props.form.getFieldValue("featuretype"),
            computationtype: this.props.form.getFieldValue("computationtype"),
            enable: this.props.form.getFieldValue('enable'),
            fixed: this.props.form.getFieldValue("fixed"),
            value: this.props.form.getFieldValue("value"),
            rule: this.props.form.getFieldValue("rule"),
        };

        let errormessage = '';
        if (typeof feature.name === 'undefined' || feature.name === '') {
            errormessage = "name vide";
        }
        if (typeof feature.description === 'undefined' || feature.description === '') {
            errormessage += " ; description vide";
        }
        if (typeof feature.classname === 'undefined' || feature.classname === '') {
            errormessage += " ; classname vide";
        }
        if (typeof feature.transfertype === 'undefined') {
            errormessage += " ; Type de Transaction vide";
        }
        if (typeof feature.featuretype === 'undefined' || feature.featuretype === '') {
            errormessage += " ; Partition vide";
        }
        if (typeof feature.computationtype === 'undefined') {
            errormessage += " ; Type de calcul empty";
        }

        if (errormessage !== '') {
            message.error(errormessage);
        } else {
            axios.post(API.API_MODIFY_FEATURE, feature)
                .then(res => {
                    message.success("Feature saved successufully");
                    this.props.fetchAllFeatures();
                    this.resetForm();
                })
        }
    }
    cancelSave = event => {
        message.error('Action canceled');
    }

    confirmAddNew = event => {
        const feature = {
            name: this.props.form.getFieldValue("name"),
            description: this.props.form.getFieldValue("description"),
            classname: this.props.form.getFieldValue("classname"),
            transfertype: this.props.form.getFieldValue("transfertype"),
            featuretype: this.props.form.getFieldValue("featuretype"),
            computationtype: this.props.form.getFieldValue("computationtype"),
            enable: this.props.form.getFieldValue('enable'),
            fixed: this.props.form.getFieldValue("fixed"),
            value: this.props.form.getFieldValue("value"),
        };
        let errormessage = '';
        if (typeof feature.name === 'undefined' || feature.name === '') {
            errormessage = "name vide";
        }
        if (typeof feature.description === 'undefined' || feature.description === '') {
            errormessage += " ; description vide";
        }
        if (typeof feature.classname === 'undefined' || feature.classname === '') {
            errormessage += " ; classname vide";
        }
        if (typeof feature.transfertype === 'undefined') {
            errormessage += " ; Type de Transaction vide";
        }
        if (typeof feature.featuretype === 'undefined' || feature.featuretype === '') {
            errormessage += " ; Partition vide";
        }
        if (typeof feature.computationtype === 'undefined') {
            errormessage += " ; Type de calcul empty";
        }

        if (errormessage !== '') {
            message.error(errormessage);
        } else {
            axios.post(API.API_ADDNEW_FEATURE, feature)
                .then(res => {
                    message.success("Feature added successufully");
                    this.props.fetchAllFeatures();
                    this.resetForm();
                })
        }
    }
    cancelAddNew = event => {
        message.error('Action canceled');
    }

    confirmEnableAllFeatures = e => {
        axios.get(API.API_ENABLE_ALL_FEATURES)
            .then((response) => {
                message.success(response.data.message)
                this.props.fetchAllFeatures();
            })
    }

    cancelEnableAllFeatures = e => {
        message.error('Action canceled');
    }

    confirmDeployAllFeatures = e => {
        axios.get(API.API_DEPLOY_ALL_FEATURES)
            .then((response) => {
                message.success(response.data.message)
            })
    }

    cancelDeployAllFeatures = e => {
        message.error('Action canceled');
    }

    //Drawer Rule of feature
    showDrawer = () => {
        this.setState({
            visible: true,
        });
    };

    onSaveRuleDrawer = () => {
        this.setState({
            visible: false,
        });
    };

    onClose = () => {
        this.setState({
            visible: false,
        });
    };
    handleChangeDrawer = e => {
        //this.setState({tmprulecontent: e.target.value});
        this.props.form.setFieldsValue({
            rule: e.target.value

        });
    }

    //Custom Filter
    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ width: 188, marginBottom: 8, display: 'block' }}
                />
                <Button
                    type="primary"
                    onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    icon="search"
                    size="small"
                    style={{ width: 90, marginRight: 8 }}
                >
                    Search
                </Button>
                <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
                    Reset
                </Button>
            </div>
        ),
        filterIcon: filtered => (
            <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => this.searchInput.select());
            }
        },
        /*render: text =>
            this.state.searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text.toString()}
                />
            ) : (
                text
            ),*/
    });

    handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex,
        });
    };

    handleReset = clearFilters => {
        clearFilters();
        this.setState({ searchText: '' });
    };

    render() {
        const propstable = {
            bordered: false,
            loading: this.props.isloading,
            //pagination: {onChange: this.handlePagination, position: "bottom", total: this.props.transfersCount},
            //size: "small",
            //title: title,
            //showHeader: true,
            //scroll: {y: 225}
        };
        const columns = [
            {
                title: "Name",
                dataIndex: "name",
                key: "name",
                render: (text, record) => (
                    <Tooltip placement="leftBottom" title={record.description}>
                        <Button type="link" onClick={(e) => {
                            this.displayDetailFeature(record, e)
                        }}>{text}</Button>
                    </Tooltip>
                ),
                ...this.getColumnSearchProps('name')
            },
            {
                title: "Engine",
                dataIndex: "computationtype",
                key: "computationtype"
            },
            {
                title: "Key",
                dataIndex: "featuretype",
                key: "featuretype",
                filters: [
                    { text: "Creditor Account", value: "ca" },
                    { text: "Debtor Account", value: "da" },
                    { text: "Debtor Account and Creditor Account", value: "daca" },
                    { text: "Debtor Account and Creditor Country", value: "dacc" },
                    { text: "Debtor Account and Creditor Bic", value: "dacb" },
                    { text: "Debtor Account and Creditor Name", value: "dacn" },
                ],
                onFilter: (value, record) => {
                    return record.featuretype === value;
                },
            },
            {
                title: "Type",
                dataIndex: "transfertype",
                key: "transfertype"
            },
            {
                title: "Actif",
                dataIndex: "enable",
                key: "enable",
                render: (text, record) => (
                    <Tag color={(text === 'Y') ? 'green' : 'volcano'}>{text}</Tag>
                ),
                filters: [
                    { text: "Enable", value: "Y" },
                    { text: "Disable", value: "N" },
                ],
                onFilter: (value, record) => {
                    return record.enable === value;
                },
            },
            {
                title: "Fixed",
                dataIndex: "fixed",
                key: "fixed",
                render: (text, record) => (
                    <Tag color={(text === 'Y') ? 'green' : 'volcano'}>{text}</Tag>
                ),
                filters: [
                    { text: "Enable", value: "true" },
                    { text: "Disable", value: "false" },
                ],
                onFilter: (value, record) => {
                    return record.fixed === value;
                },
            },
            {
                title: "Creation Date",
                dataIndex: "creationdate",
                key: "creationdate"
            },
            {
                title: "Last Modfication",
                dataIndex: "modificationdate",
                key: "modificationdate"
            }
        ]
        const { getFieldDecorator } = this.props.form;

        const formItemLayouta = {
            labelCol: { span: 6 },
            wrapperCol: { span: 14 },
        };
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };

        return (
            <Fragment>
                <Row>
                    <Col span={16}>
                        <Card size="small" title="Available Features">
                            <Row>
                                <Col span={24}>
                                    <Table {...propstable} columns={columns} dataSource={this.props.features} className="table-striped-rows" />
                                </Col>
                                <Col span={24} style={{ textAlign: 'right' }}>
                                    <Popconfirm
                                        title="Are you sure enable all futures ?"
                                        onConfirm={this.confirmEnableAllFeatures}
                                        onCancel={this.cancelEnableAllFeatures}
                                        okText="Yes"
                                        cancelText="No"
                                    >
                                        <Button type="primary" htmlType="submit" style={{ marginLeft: 8 }}>
                                            Enable All
                                        </Button>

                                    </Popconfirm>
                                    <Popconfirm
                                        title="Are you sure deploy all futures ?"
                                        onConfirm={this.confirmDeployAllFeatures}
                                        onCancel={this.cancelDeployAllFeatures}
                                        okText="Yes"
                                        cancelText="No">
                                        <Button type="primary" htmlType="submit" style={{ marginLeft: 8 }}>
                                            Deploy Features
                                        </Button>
                                    </Popconfirm>
                                </Col>
                            </Row>
                        </Card>
                    </Col>



                    <Col span={8}>
                        <Card size="small" title="Detail" style={{ marginLeft: '5px' }}>
                            <Form {...formItemLayouta}>
                                <Row>
                                    <Col span={24}>
                                        <Form.Item label="Identifier">
                                            {getFieldDecorator('id', {})(
                                                <Input
                                                    disabled={true} />
                                            )}
                                        </Form.Item>

                                        <Form.Item label="Name">
                                            {getFieldDecorator('name', {
                                                rules: [{
                                                    required: true,
                                                    message: 'Please input the name of the feature!'
                                                }],
                                            })(
                                                <Input
                                                    placeholder="Feature Name" />,
                                            )}
                                        </Form.Item>

                                        <Form.Item label={"description"}>
                                            {getFieldDecorator('description', {
                                                rules: [{
                                                    required: true,
                                                    message: 'Please input the description of the feature!'
                                                }],
                                            })(
                                                <TextArea
                                                    placeholder="Description"
                                                    className='form-control'
                                                    style={{ fontSize: '14px' }}
                                                    rows='3' cols='10' />,
                                            )}
                                        </Form.Item>

                                        <Form.Item label="Transaction Type" hasFeedback>
                                            {getFieldDecorator('transfertype', {})(
                                                <Select defaultValue="all">
                                                    <Option value="all">All</Option>
                                                    <Option value="swift">Swift</Option>
                                                    <Option value="sepa">Sepa</Option>
                                                    <Option value="cash_in">Cash_in</Option>
                                                    <Option value="cash_out">Cash_out</Option>
                                                    <Option value="transfer">Transfer</Option>
                                                    <Option value="payment">Payment</Option>
                                                </Select>
                                            )}
                                        </Form.Item>

                                        <Form.Item label="Partition" hasFeedback>
                                            {getFieldDecorator('featuretype', {})(
                                                <Select defaultValue="da">
                                                    <Option value="da">Debtor Account</Option>
                                                    <Option value="ca">Creditor Account</Option>
                                                    <Option value="daca">Debtor Account and Creditor Account</Option>
                                                    <Option value="dacb">Debtor Account and Creditor Bic</Option>
                                                    <Option value="dacc">Debtor Account and Creditor Country</Option>
                                                    <Option value="dacn">Debtor Account and Creditor Name</Option>
                                                </Select>
                                            )}
                                        </Form.Item>

                                        <Form.Item label="Engine Type" hasFeedback>
                                            {getFieldDecorator('computationtype', {})(
                                                <Select defaultValue="engine">
                                                    <Option value="engine">Engine</Option>
                                                    <Option value="redis">Key-Value</Option>
                                                    <Option value="psql">Database</Option>
                                                </Select>
                                            )}
                                        </Form.Item>

                                        <Form.Item label="Feature Body">
                                            {getFieldDecorator('classname', {
                                                rules: [{
                                                    required: true,
                                                    message: 'Please input the class name of the feature!'
                                                }],
                                            })(
                                                <Input
                                                    placeholder="Class Name" />,
                                            )}
                                        </Form.Item>

                                        <Form.Item label={"Rule"}>

                                            <Row>
                                                <Col span={12} style={{ textAlign: 'left' }}>
                                                    {getFieldDecorator('rule', {})(
                                                        <TextArea
                                                            disabled={true}
                                                            value={this.state.tmpruleContent}
                                                            placeholder="Rule"
                                                            className='form-control'
                                                            style={{ fontSize: '14px' }}
                                                            rows='2' cols='5' />,
                                                    )}
                                                </Col>
                                                <Col span={12} style={{ textAlign: 'right' }}>
                                                    <Button type="primary" onClick={this.showDrawer}>
                                                        <Icon type="plus" /> Create / Modify Rule
                                                    </Button>
                                                </Col>
                                            </Row>

                                            <Drawer
                                                title={"Create a new Rule for this feature: [" + this.props.form.getFieldValue("name") + "]"}
                                                width={920}
                                                onClose={this.onClose}
                                                visible={this.state.visible}
                                                bodyStyle={{ paddingBottom: 80 }}
                                            >
                                                <Row gutter={16}>
                                                    <Col span={24}>
                                                        <Form.Item label="Content">
                                                            {getFieldDecorator('tmprule', {})(
                                                                <TextArea
                                                                    onChange={this.handleChangeDrawer}
                                                                    placeholder="Rule Content"
                                                                    className='form-control'
                                                                    style={{ fontSize: '14px' }}
                                                                    rows='30' cols='10' />,
                                                            )}
                                                        </Form.Item>
                                                    </Col>
                                                </Row>
                                                <div
                                                    style={{
                                                        position: 'absolute',
                                                        right: 0,
                                                        bottom: 0,
                                                        width: '100%',
                                                        borderTop: '1px solid #e9e9e9',
                                                        padding: '10px 16px',
                                                        textAlign: 'right',
                                                    }}
                                                >
                                                    <Button onClick={this.onClose} style={{ marginRight: 8 }}>
                                                        Cancel
                                                    </Button>
                                                    <Button onClick={this.onSaveRuleDrawer} type="primary">
                                                        <Icon type="check" />Save
                                                    </Button>
                                                </div>
                                            </Drawer>


                                        </Form.Item>


                                        <Form.Item label="Enable">
                                            {getFieldDecorator('enable', { valuePropName: 'checked' })(
                                                <Switch />)}
                                        </Form.Item>

                                        <Form.Item label="Fixed value">
                                            <Row gutter={8}>
                                                <Col span={12}>
                                                    {getFieldDecorator('fixed', { valuePropName: 'checked' })(
                                                        <Switch />
                                                    )}
                                                </Col>
                                                <Col span={12}>
                                                    {getFieldDecorator('value', { initialValue: 0.0 })(
                                                        <InputNumber min={0} />
                                                    )}
                                                </Col>

                                            </Row>
                                        </Form.Item>
                                    </Col>
                                    <Col span={24} style={{ textAlign: 'right' }}>
                                        <Popconfirm
                                            title="Are you sure add Future ?"
                                            onConfirm={this.confirmAddNew}
                                            onCancel={this.cancelAddNew}
                                            okText="Yes"
                                            cancelText="No"
                                        >
                                            <Button type="primary" htmlType="submit">
                                                Add New
                                            </Button>
                                        </Popconfirm>

                                        <Popconfirm
                                            title="Are you sure save Future ?"
                                            onConfirm={this.confirmSave}
                                            onCancel={this.cancelSave}
                                            okText="Yes"
                                            cancelText="No"
                                        >
                                            <Button style={{ marginLeft: 8 }} type="primary" htmlType="submit">
                                                Save
                                            </Button>
                                        </Popconfirm>
                                        <Button style={{ marginLeft: 8 }} onClick={this.resetForm}>
                                            Reset
                                        </Button>
                                    </Col>
                                </Row>

                            </Form>
                        </Card>
                    </Col>
                </Row>

            </Fragment>

        );
    }
}

const WrappedHorizontalFraudFeatures = Form.create({ name: 'featuredesigner' })(BsDesignerFeatures);
const mapStateToProps = (state) => {
    return state
};
export default connect(mapStateToProps, actionCreators)(WrappedHorizontalFraudFeatures);
