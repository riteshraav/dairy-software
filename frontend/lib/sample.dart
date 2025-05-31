import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:take8/providers/buffalo_ratechart_provider.dart';
import 'package:take8/providers/cow_ratechart_provider.dart';

import '../../widgets/appbar.dart';

class ExcelViewer extends StatefulWidget {
  String excelType;
  ExcelViewer(this.excelType);
  @override
  _ExcelViewerState createState() => _ExcelViewerState();
}

class _ExcelViewerState extends State<ExcelViewer> {
  List<List<String>>? _rateGrid;
  String? _fileName;
  bool _isLoading = false;
  bool _hasChanges = false;
  bool isAdjusting = false;
  bool entireChart = true;
  int row = 0,col = 0;
  TextEditingController _adjustController = TextEditingController();
  int? _selectedColumnIndex;
  int? _selectedRowIndex;
  bool? _isChangeEntire;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _pickAndReadExcel();
  }
  Map<String, List<List<String>>> _rateHistory = {};
  String? _selectedHistoryKey;

  Future<void> _pickAndReadExcel() async {
    setState(() {

      _isLoading = true;
      _rateGrid = null;
      _fileName = null;
      _hasChanges = false;
      _selectedColumnIndex = null;
      _selectedRowIndex = null;
      _isChangeEntire = null;
      _rateHistory.clear();
    });

    // final result = await FilePicker.platform.pickFiles(
    //   type: FileType.custom,
    //   allowedExtensions: ['xlsx'],
    //   withData: true,
    // );
    String name ;
    List<List<String>> rateData;

    if(widget.excelType == 'cow') {
      name = Provider.of<CowRateChartProvider>(context,listen: true).name;
      rateData = Provider.of<CowRateChartProvider>(context,listen: true).excelData;
      row = Provider.of<CowRateChartProvider>(context,listen: true).row! - 1;
      col = Provider.of<CowRateChartProvider>(context,listen: true).col!;

    } else{
      name = Provider.of<BuffaloRatechartProvider>(context,listen: true).name;
      col = Provider.of<BuffaloRatechartProvider>(context,listen: true).col!;
      row = Provider.of<BuffaloRatechartProvider>(context,listen: true).row! - 1;

      rateData = Provider.of<BuffaloRatechartProvider>(context,listen: true).excelData;
    }
    print("row is $row  col is ${col}");

    // for (var table in excel.tables.keys) {
    //   final sheet = excel.tables[table];
    //   if (sheet != null && sheet.rows.isNotEmpty) {
    //     for (var row in sheet.rows) {
    //       List<String> parsedRow = row.map((cell) {
    //         if (cell == null || cell.value == null) return '';
    //         return cell.value.toString();
    //       }).toList();
    //       if (parsedRow.any((value) => value.trim().isNotEmpty)) {
    //         rateData.add(parsedRow);
    //       }
    //     }
    //   }
    //   break;
    // }
    setState(() {
      _rateGrid = rateData;
      _fileName = name;
      _isLoading = false;
    });

  }

  void _showAdjustmentOptionDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) {
        bool tempChoice = true;
        return StatefulBuilder(
          builder: (context, setDialogState) {
            return AlertDialog(
              title: Text("Select Adjustment Mode"),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  RadioListTile<bool>(
                    title: Text("Change Entire Rate Table"),
                    value: true,
                    groupValue: tempChoice,
                    onChanged: (value) {
                      setDialogState(() => tempChoice = value!);
                    },
                  ),
                  RadioListTile<bool>(
                    title: Text("Change Specific Rate Only"),
                    value: false,
                    groupValue: tempChoice,
                    onChanged: (value) {
                      setDialogState(() => tempChoice = value!);
                    },
                  ),
                ],
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(ctx).pop(),
                  child: Text("Cancel"),
                ),
                CustomWidgets.customButton(
                  text: "Ok",
                  onPressed: () {
                    this.setState(() {
                      _isChangeEntire = tempChoice;
                      isAdjusting = true;
                    });
                    Navigator.of(ctx).pop();
                  },
                )
              ],
            );
          },
        );
      },
    );

  }

  void _applyAdjustment() {
    if (_rateGrid == null || _adjustController.text.trim().isEmpty) return;

    double? adjustment = double.tryParse(_adjustController.text.trim());
    if (adjustment == null) return;

    List<List<String>> updated = _rateGrid!.map((row) => List<String>.from(row)).toList();

    for (int i = 0; i < updated.length; i++) {
      if(i == row)continue;
      for (int j = 0; j < updated[i].length; j++) {
        if(col == j)
        {
          continue;
        }
        bool shouldAdjust = false;

        if (_isChangeEntire == true) {
          shouldAdjust = true;
        } else {
          if (_selectedColumnIndex != null && j == _selectedColumnIndex) {
            shouldAdjust = true;
          }
          if (_selectedRowIndex != null && i == _selectedRowIndex) {
            shouldAdjust = true;
          }
        }

        if (shouldAdjust) {
          String cellValue = updated[i][j].trim().replaceAll(RegExp(r'[^\d\.\-]'), '');
          double? original = double.tryParse(cellValue);
          if (original != null) {
            double adjusted = original + adjustment;
            updated[i][j] = adjusted.toStringAsFixed(2);
          }
        }
      }
    }
    setState(() {
      _rateGrid = updated;
      _hasChanges = true;
      _isChangeEntire = false;
      isAdjusting = false;
    });
  }

  void _restoreFromHistory(String key) {
    setState(() {
      _rateGrid = _rateHistory[key]!.map((r) => List<String>.from(r)).toList();
      _selectedHistoryKey = key;
    });
  }

  void _showHistoryDialog() {
    List<String> historyKeys = _rateHistory.keys.toList().reversed.toList();

    showDialog(
      context: context,
      builder: (ctx) {
        String? dropdownValue = historyKeys.isNotEmpty ? historyKeys.first : null;

        return StatefulBuilder(
          builder: (context, setState) => AlertDialog(
            title: Text("View History"),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                if (historyKeys.isNotEmpty)
                  DropdownButton<String>(
                    isExpanded: true,
                    value: dropdownValue,
                    items: historyKeys
                        .map((key) => DropdownMenuItem(
                      value: key,
                      child: Text(key), // shows only date
                    ))
                        .toList(),
                    onChanged: (value) {
                      setState(() {
                        dropdownValue = value;
                        _restoreFromHistory(value!);
                      });
                    },
                  )
                else
                  Text("No history available."),
              ],
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(ctx).pop(),
                child: Text("Close"),
              ),
            ],
          ),
        );
      },
    );
  }

  void _saveChanges() {
    if (!_hasChanges) return;

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text("Save Confirmation"),
        content: Text("Are you sure you want to save the changes?"),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(),
            child: Text("Cancel"),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.of(ctx).pop();
              setState(() {
                _hasChanges = false;
              });
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text("Changes saved!")),
              );
            },
            child: Text("Save"),
          ),
        ],
      ),
    );
  }

  Widget _buildRateTable() {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: SingleChildScrollView(
        scrollDirection: Axis.vertical,
        child: Table(
          border: TableBorder.all(color: Colors.black12),
          defaultColumnWidth: IntrinsicColumnWidth(),
          children: _rateGrid!.asMap().entries.map((entry) {
            int rowIndex = entry.key;
            List<String> row = entry.value;
            final isHeader = rowIndex == 0;

            return TableRow(
              children: row.asMap().entries.map((cellEntry) {
                int colIndex = cellEntry.key;
                String cell = cellEntry.value;

                bool isFatColumn = colIndex == 0;
                bool isSelectedColumn = colIndex == _selectedColumnIndex;
                bool isSelectedRow = rowIndex == _selectedRowIndex;

                return GestureDetector(
                  onTap: () {
                    if (isHeader && colIndex != 0) {
                      setState(() {
                        _selectedColumnIndex = colIndex;
                        _selectedRowIndex = null;
                      });
                    } else if (!isHeader && _isChangeEntire == false) {
                      setState(() {
                        _selectedRowIndex = rowIndex;
                        _selectedColumnIndex = null;
                      });
                    }
                  },
                  child: Container(
                    padding: EdgeInsets.all(8),
                    alignment: Alignment.center,
                    color: isHeader
                        ? (isSelectedColumn ? Colors.orange[200] : Color(0xFFe3f3fb))
                        : isFatColumn
                        ? Colors.yellow[100]
                        : isSelectedRow
                        ? Colors.lightBlue[50]
                        : Colors.white,
                    child: Text(
                      cell,
                      style: TextStyle(
                          fontWeight: isHeader ? FontWeight.bold : FontWeight.normal),
                    ),
                  ),
                );
              }).toList(),
            );
          }).toList(),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back,color: Colors.white,), onPressed: () {
          Navigator.of(context).pop();
        },
        ),
        title: Text("Rate Viewer" ,style: TextStyle(color: Colors.white),
        ),
        backgroundColor: Color(0xFF24A1DE),

        actions: [
          IconButton(
            icon: Icon(Icons.history,color: Colors.white,),
            tooltip: "View History",
            onPressed: _showHistoryDialog,
          ),
          if (_hasChanges)
            IconButton(
              icon: Icon(Icons.save,color: Colors.white,),
              onPressed: _saveChanges,
            ),
        ],
      ),
      body:  (_isLoading) ?  Center(
        child: CircularProgressIndicator(
          strokeWidth: 5,
          valueColor: AlwaysStoppedAnimation<Color>(Colors.blue), // change color
        ),
      ):Column(
        children: [
          //   child: ElevatedButton(
          //     onPressed: _pickAndReadExcel,
          //     style: ElevatedButton.styleFrom(
          //       backgroundColor: Color(0xFF24A1DE),
          //       shape: StadiumBorder(),
          //       padding: EdgeInsets.symmetric(horizontal: 30, vertical: 15),
          //     ),
          //     child: Text("Open File", style: TextStyle(color: Colors.white)),
          //   ),
          // ),
          SizedBox(height: 10),
          if (_rateGrid != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20.0),
              child: isAdjusting?
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _adjustController,
                      keyboardType: TextInputType.numberWithOptions(decimal: true),
                      decoration: InputDecoration(labelText: 'Adjustment (+/-)',
                          border: OutlineInputBorder()
                      ),
                    ),
                  ),
                  SizedBox(width: 10),
                  CustomWidgets.customButton(text:  "Apply",onPressed: _applyAdjustment),
                ],

              ):
              CustomWidgets.customButton(text: 'Adjust Rate', onPressed: () {
                _showAdjustmentOptionDialog();
              },)
              ,

            ),

          SizedBox(height: 10),
          Expanded(
            child: _isLoading
                ? Center(child: CircularProgressIndicator())
                : _rateGrid != null
                ? Padding(
              padding: const EdgeInsets.all(8.0),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    SizedBox(height: 10),
                    if (_fileName != null)
                      Text("File: $_fileName", style: TextStyle(fontWeight: FontWeight.bold,fontSize: 20)),
                    SizedBox(height: 10),
                    Container(
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.black26),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: _buildRateTable(),
                    ),
                  ],
                ),
              ),
            )
                : Center(child: Text("No file selected.")),
          ),
        ],
      ),
    );
  }
}