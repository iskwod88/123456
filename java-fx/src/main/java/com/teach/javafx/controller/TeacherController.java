package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.*;
import javafx.scene.Scene;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.util.CommonMethod;
import com.teach.javafx.controller.base.MessageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherController {
    private ImageView photoImageView;
    @FXML
    private TableView<Map> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Map, String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map, String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Map, String> deptColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Map, String> titleColumn; //学生信息表 专业列
    @FXML
    private TableColumn<Map, String> degreeColumn; //学生信息表 班级列
    @FXML
    private TableColumn<Map, String> responseColumn; //学生信息表 班级列
    @FXML
    private TableColumn<Map, String> cardColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<Map, String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<Map, String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<Map, String> emailColumn; //学生信息表 邮箱列
    @FXML
    private TableColumn<Map, String> phoneColumn; //学生信息表 电话列
    @FXML
    private TableColumn<Map, String> addressColumn;//学生信息表 地址列

    //照片有待补充

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField titleField;
    @FXML
    private TextField degreeField;
    @FXML
    private TextField responseField;
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域

    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer personId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> teacherList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private void setTableViewData() {
        observableList.clear();
        for (int i = 0; i < teacherList.size(); i++) {
            observableList.addAll(FXCollections.observableArrayList(teacherList.get(i)));
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    public void initialize() {
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        //照片有待补充
        DataResponse res;
        DataRequest req = new DataRequest();
        req.add("numName", "");
        res = HttpRequestUtil.request("/api/teacher/getTeacherList", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            teacherList = (ArrayList<Map>) res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        deptColumn.setCellValueFactory(new MapValueFactory<>("dept"));
        titleColumn.setCellValueFactory(new MapValueFactory<>("title"));
        degreeColumn.setCellValueFactory(new MapValueFactory<>("degree"));
        responseColumn.setCellValueFactory(new MapValueFactory<>("response"));
        cardColumn.setCellValueFactory(new MapValueFactory<>("card"));
        genderColumn.setCellValueFactory(new MapValueFactory<>("genderName"));
        birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new MapValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new MapValueFactory<>("address"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");

        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    public void clearPanel() {
        personId = null;
        numField.setText("");
        nameField.setText("");
        deptField.setText("");
        titleField.setText("");
        degreeField.setText("");
        responseField.setText("");
        cardField.setText("");
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    protected void changeTeacherInfo() {
        Map<String,Object> form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            clearPanel();
            return;
        }
        personId = CommonMethod.getInteger(form, "personId");
        DataRequest req = new DataRequest();
        req.add("personId", personId);
        DataResponse res = HttpRequestUtil.request("/api/teacher/getTeacherInfo", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        deptField.setText(CommonMethod.getString(form, "dept"));
        titleField.setText(CommonMethod.getString(form, "title"));
        degreeField.setText(CommonMethod.getString(form, "degree"));
        responseField.setText(CommonMethod.getString(form, "response"));
        cardField.setText(CommonMethod.getString(form, "card"));
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, CommonMethod.getString(form, "gender")));
        birthdayPick.getEditor().setText(CommonMethod.getString(form, "birthday"));
        emailField.setText(CommonMethod.getString(form, "email"));
        phoneField.setText(CommonMethod.getString(form, "phone"));
        addressField.setText(CommonMethod.getString(form, "address"));
        //照片有待补充
    }

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change) {
        changeTeacherInfo();
    }

    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName", numName);
        DataResponse res = HttpRequestUtil.request("/api/teacher/getTeacherList", req);
        if (res != null && res.getCode() == 0) {
            teacherList = (ArrayList<Map>) res.getData();
            setTableViewData();
        }
    }

    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }

    //删除有待补充

    @FXML
    protected void onSaveButtonClick() {
        if (numField.getText().isEmpty()) {
            MessageDialog.showDialog("工号为空，不能修改");
            return;
        }
        Map<String,Object> form = new HashMap<>();
        form.put("num", numField.getText());
        form.put("name", nameField.getText());
        form.put("dept", deptField.getText());
        form.put("title", titleField.getText());
        form.put("degree", degreeField.getText());
        form.put("response", responseField.getText());
        form.put("card", cardField.getText());
        if (genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender", genderComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("birthday", birthdayPick.getEditor().getText());
        form.put("email", emailField.getText());
        form.put("phone", phoneField.getText());
        form.put("address", addressField.getText());
        DataRequest req = new DataRequest();
        req.add("personId", personId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/teacher/teacherEditSave", req);
        if (res.getCode() == 0) {
            personId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("wcsndm！");
            onQueryButtonClick();
        } else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void doNew() {
        clearPanel();
    }

    public void doSave() {
        onSaveButtonClick();
    }

    //删除的重写有待补充

    @FXML
    protected void onAssistantButtonClick() {
        DataRequest req = new DataRequest();
        req.add("personId", personId);
        DataResponse res = HttpRequestUtil.request("/api/teacher/getTeacherAssistantById", req);
        //默认ID排序，其他方法有待补充修改
        if(res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        List<Map> assistantList = (List<Map>) res.getData();
        ObservableList<Map> oList = FXCollections.observableArrayList(assistantList);
        Scene scene = null, pScene = null;
        Stage stage = new Stage();
        TableView<Map> table = new TableView<>(oList);
        table.setEditable(true);

        TableColumn<Map, String> assistantNameColumn = new TableColumn<>("助理姓名");
        assistantNameColumn.setCellValueFactory(new MapValueFactory<>("assistantName"));
        assistantNameColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        table.getColumns().add(assistantNameColumn);

        TableColumn<Map, String> assistantNumColumn = new TableColumn<>("助理学号");
        assistantNumColumn.setCellValueFactory(new MapValueFactory<>("assistantNum"));
        assistantNumColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        table.getColumns().add(assistantNumColumn);

        TableColumn<Map, String> wageColumn = new TableColumn<>("工资");
        wageColumn.setCellValueFactory(new MapValueFactory<>("wage"));
        wageColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        table.getColumns().add(wageColumn);

        TableColumn<Map, String> timeColumn = new TableColumn<>("工作日期");
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        timeColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        table.getColumns().add(timeColumn);

        BorderPane root = new BorderPane();
        FlowPane flowPane = new FlowPane();

        Button obButton = new Button("呃呃呃");
        obButton.setOnAction( event ->{
            for(Map map : table.getItems()) {
                System.out.println("Map:" + map);
            }
            stage.close();
        });
        //关闭按钮

        flowPane.getChildren().add(obButton);
        root.setCenter(table);
        root.setBottom(flowPane);
        scene = new Scene(root, 260, 140);
        stage.initOwner(MainApplication.getMainStage());
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("WCSNDM！");
        stage.setOnCloseRequest(event -> {
            MainApplication.setCanClose(true);
        });
        stage.showAndWait();
    }

    //excel、家庭成员、照片、工资有待补充
























}
