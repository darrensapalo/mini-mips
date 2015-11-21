//////////////////////////
//      Utility        //
////////////////////////

function populateTable(tableID, data){

  var th = d3.select(tableID).select("thead").selectAll("th").data(d3.keys(data[0]));
  th.remove();
  th.exit().remove();

  d3.select(tableID).select("thead").selectAll("th")
  .data(d3.keys(data[0]))
  .enter().append("th").text(function(d){return d});


  var tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);
  tableRows.remove();
  tableRows.exit().remove();

  tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);

  tableRows.enter()
    .append('tr')
      .selectAll('td')
      .data(function(d){return d3.values(d);})
      .enter()
      .append('td')
        .text(function(d){return d;})
        .attr('class', 'text-nowrap')
        .attr('contenteditable', function(d){
          if(d.length == 16 && d.indexOf(' ') < 0)
            return true;
          return false;
        });
}