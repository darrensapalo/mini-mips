//////////////////////////
//      Utility        //
////////////////////////

var opcodeColumns = ['mem', 'opcode', 'instruction'];
var registerColumns = ['register', 'value'];
var memoryColumns = ['address', 'value'];
var pipelineColumns = ['cycle', 'if', 'id', 'ex', 'mem', 'wb'];


function populateTable(tableID, data, columns){

  var th = d3.select(tableID).select("thead").selectAll("th").data(columns);
  th.remove();
  th.exit().remove();

  d3.select(tableID).select("thead").selectAll("th")
  .data(columns)
  .enter().append("th").text(function(d){return d});


  var tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);
  tableRows.remove();
  tableRows.exit().remove();

  tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);

  tableRows.enter()
    .append('tr')
      .selectAll('td')
      .data(function(d){

        var row = [];

        for(i=0;i<columns.length;i++){
          row.push(d[columns[i]]);
        }

        return row;

      })
      .enter()
      .append('td')
        .text(function(d){return d;})
        .attr('class', 'text-nowrap')
        .attr('contenteditable', function(d){
          if(d.length == 16 && d.indexOf(' ') < 0 || d.length == 8 && tableID == '#table-memory' )
            return true;
          return false;
        });
}

function populatePipelineTable(data){

  if(data.length == 0)
    return;

  var tableID = "#table-cpu-pipeline";
  var pipelineData = data["pipeline"];
  var columns = [];
  columns.push("Instruction");
  for(i=0;i<data["cycles"];i++)
    columns.push(i+1);

  /* Table Headers */
  var th = d3.select(tableID).select("thead").selectAll("th").data(columns);
  th.remove();
  th.exit().remove();

  d3.select(tableID).select("thead").selectAll("th")
  .data(columns)
  .enter().append("th").text(function(d){return d});

  /* Table Content */
  var tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(pipelineData);
  tableRows.remove();
  tableRows.exit().remove();

  tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(pipelineData);

  tableRows.enter()
    .append('tr')
      .selectAll('td')
      .data(function(d){
        var row = [];
        row.push(d["instruction"]);
        row = row.concat(d.records);
        return row;
      })
      .enter()
      .append('td')
        .text(function(d){
          return d;})
        .attr('class', 'text-nowrap')
        .attr('contenteditable', function(d){
          if(d && d.length == 16 && d.indexOf(' ') < 0)
            return true;
          return false;
        });

}
