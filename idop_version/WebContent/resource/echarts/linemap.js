var templtOption = {
	configColor:[
	             "#ea5555",
	             "#F08c48",
	             "#48D5DD",
	             "#ea5594",
	             "#f0ee48",
	             "#48acdb",
	             "#A8d940",
	             "#4fe2bf",
	             "#4fe275",
	             "#efd036",
	             "#eead40",
	             "#1fc1de",
	             "#d26ddb",
	             "#40c2b1",
	             "#c4509f",
	             "#50a6c4",
	             "#b66ddb",
	             "#4083c2"
	            ],
	ywtypeColor:["#f05d5d","#f7af13","#5fc3c8","#4083c2","#50a6c4"],
	line_for_smooth:{
		title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltip:{
				x:"center",
				axisPointer:{type:'cross'}
				},
			legend:{
				data:['扬州分行','南通分行',"平均值"]
				},
			grid:{
				left:'6%',
				right:'8%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	   data:['2018.01','2018.02','2018.03','2018.04','2018.05','2018.06','2018.07','2018.08','2018.09','2018.10','2018.11','2018.12']
			       }
			      ],
			yAxis:[
			       { 
			    	  type:'value', 
			       }
			       ],
			series:[
						{
						name:'扬州分行',
						type:'line',
						smooth:true,
					    data:[91.55,91.43,87.88,79.66,76.37,84.97,89.18,91.05,90.11,88.83,87.97,86.6]
						},
						{
						name:'南通分行',
						type:'line',
						smooth:true,
						data:[86.55,87.43,89.88,90.66,91.37,89.97,88.18,87.05,86.11,85.83,84.97,83.6]
						},
						{
						name:'平均值',
						type:'line',
						smooth:true,
						data:[89.05,89.43,88.88,85.16,83.515,87.47,88.68,89.05,88.11,87.83,86.47,85.1]
						}
						
			     ]
	},
	line_for_smooth1:{
		title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltip:{
				x:"center",
				axisPointer:{type:'cross'}
				},
			legend:{
				data:['扬州分行','南通分行',"平均值"]
				},
			grid:{
				left:'6%',
				right:'8%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	   data:['2018.01','2018.02','2018.03','2018.04','2018.05','2018.06','2018.07','2018.08','2018.09','2018.10','2018.11','2018.12']
			       }
			      ],
			yAxis:[
			       { 
			    	  type:'value', 
			       }
			       ],
			series:[
						{
						name:'扬州分行',
						type:'line',
						smooth:true,
					    data:[91.55,91.43,87.88,79.66,76.37,84.97,89.18,91.05,90.11,88.83,87.97,86.6]
						},
						{
						name:'南通分行',
						type:'line',
						smooth:true,
						data:[86.55,87.43,89.88,90.66,91.37,89.97,88.18,87.05,86.11,85.83,84.97,83.6]
						},
						{
						name:'平均值',
						type:'line',
						smooth:true,
						data:[89.05,89.43,88.88,85.16,83.515,87.47,88.68,89.05,88.11,87.83,86.47,85.1]
						}
			     ]
	},
	line:{
		title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
				tooltip:{
					trigger:'axis',
					axisPointer:{type:'line'}
					},
			legend:{
				textStyle:{fontSize:14},
				data:['淮安分行','扬州分行','南通分行']
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	    axisLabel:{interval:0,rotate:40},
			    	   data:['2018.01','2018.02','2018.03','2018.04','2018.05','2018.06','2018.07','2018.08','2018.09','2018.10','2018.11','2018.12']
			       }
			      ],
			yAxis:[
			       { 
			    	  type:'value', 
			       }
			       ],
			series:[ 
				{
				name:'扬州分行',
				type:'line',
			    data:[1,2,3,4,5,6,7,8,9,10,11,12]
				},
				{
				name:'南通分行',
				type:'line',
			    data:[3,4,5,4,3,5,6,5,3,5,8,7]
				}]
	},
	
	line_temp:{
		title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
				tooltip:{
					trigger:'axis',
					axisPointer:{type:'line'}
					},
			legend:{
				textStyle:{fontSize:14},
				data:['预警数量','问题数量']
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	    axisLabel:{interval:0,rotate:40},
			    	   data:['2018.01','2018.02','2018.03','2018.04','2018.05','2018.06','2018.07','2018.08','2018.09','2018.10','2018.11','2018.12']
			       }
			      ],
			yAxis:[
			       { 
			    	  type:'value', 
			       }
			       ],
			series:[ 
				{
				name:'预警数量',
				type:'line',
			    data:[4,5,6,4,5,6,7,8,9,10,11,12]
				},
				{
				name:'问题数量',
				type:'line',
			    data:[3,4,5,4,3,5,6,5,3,5,8,7]
				}]
	},
	radar:{
		title: {},
    	tooltip: {
    	    
    	},
    	legend:{
    		top:"bottom",
    		origent:"horizontal",
    		
    		data:['南京分行','无锡分行','徐州分行'],
    	},
	    radar: [{
	    	indicator : [{text:'集中核准',max : 100},
		                    {text:'集中作业',max : 100},
		                 	{text:'现金出纳',max : 100},
		                    {text:'综合账务',max : 100},
		                    {text:'智能柜台',max : 100}
		                ],
	    	center: ['50%', '50%'],
	    	radius: 100	    	
	    }],
	    //数值序列
	    series: [{
	      type: 'radar',
	      tooltip: {
	    	    trigger:'item',
	      		position: function(p){
	      			//（数据展示图）鼠标右下角显示
	      			return [p[0] + 10, p[1] - 10];
	      		}
	      },
	      
	      avoidLabelOverlap: false,
	      areaStyle:{normal:{opacity:0}},
	      itemStyle:{normal:{areaStyle:{type:'default'},opacity:1}},
	      data: [
	             {
	            	 name:'南京分行',
	            	 
	            	 value:[66.41,88.87,89.88,100,99.43]
	             },
	             {
	            	 name:'无锡分行',
	            	 value:[77.41,66.77,90.88,100,98.43]
	             },
	             {
	            	 name:'徐州分行',
	            	 value:[89.41,77.87,87.88,100,96.00]
	             }
	      ]
	    }]
	},
	radar1:{
		title: {},
    	tooltip: {
    	    
    	},
    	legend:{
    		top:"bottom",
    		origent:"horizontal",
    		
    		data:['南京分行'],
    	},
	    radar: [{
	    	indicator : [{text:'集中核准',max : 100},
		                    {text:'集中作业',max : 100},
		                 	{text:'现金出纳',max : 100},
		                    {text:'综合账务',max : 100},
		                    {text:'智能柜台',max : 100}
		                ],
	    	center: ['50%', '50%'],
	    	radius: 100,
	    	triggerEvent:true
	    }],
	    //数值序列
	    series: [{
	      type: 'radar',
	      tooltip: {
	    	    trigger:'item',
	      		position: function(p){
	      			//（数据展示图）鼠标右下角显示
	      			return [p[0] + 10, p[1] - 10];
	      		}
	      },
	      
	      avoidLabelOverlap: false,
	      areaStyle:{
	    	  normal:{opacity:0.85},
	      	 
	      },
	      color:{
      		  type:"radial",
      		  x:0.5,
      		  y:0.5,
      		  r:1,
      		  colorStops:[
      		              {
      		            	  offset:0,
      		            	  color:'#ff3162'
      		              },
      		              {
      		            	  offset:1,
      		            	  color:'#ff0103'
      		              }
      		              ],
      		  globalCoord:false
      		  
      		  
      	  },
	      //itemStyle:{normal:{areaStyle:{type:'default'}}},
	      data: [
	             {
	            	 name:'南京分行',
	            	 label:{
							normal:{
								show:true,
								color:"#201f35",
								position:'insideRight'
							}
						},
	            	 value:[66.41,88.87,89.88,100,99.43]
	             }
	      ]
	    }]
	},
	bar:{
		title:{
			text:'',
			textStyle:{color:'#c23531',fontSize:14}
			},
		tooltip:{
			trigger:'axis',
			axisPointer:{type:'shadow'}
			},
		legend:{
			data:null
			},
		grid:{
			left:'3%',
			right:'4%',
			bottom:'3%',
			containLabel:true
			},
		xAxis:[
		       {
		    	   type:'category',
		    	   data:[
		    	   		'集中核准','集中作业','现金出纳','综合账务','智能柜台'
		    	   ]
		       }
		       
		      ],
			
			
		yAxis:[
		       {
		    	  type:'value', 
		    	  data:null
		       }
		       ],
			
		series:[{
			name:'',
			type:'bar',
			//itemStyle:{normal:{color:'#0099cc'}},
			
			data:[86.55,87.43,89.88,90.66,91.37]
			}]
	},
	bar_stack:{
		title:{
			text:'',
			textStyle:{color:'#c23531',fontSize:14}
			},
		tooltip:{
			trigger:'axis',
			axisPointer:{type:'shadow'}
			},
		legend:{
			data:['集中授权','中心关键指标','智能柜台','现金出纳']
			},
		grid:{
			left:'3%',
			right:'4%',
			bottom:'3%',
			containLabel:true
			},
		xAxis:[
		       {
		    	   type:'category',
		    	   axisLabel:{interval:0,rotate:40},
		    	   data:['南京分行','无锡分行','徐州分行','常州分行','苏州分行']
		       }
		      ],
		yAxis:[{type:'value',}],

		series:[
		        {
					name:'集中授权',
					type:'bar',
					stack:'总量',
					label:{
						normal:{
							show:true,
							position:'insideRight'
						}
					},
					itemStyle:{
						normal:{
							color:"#f05d5d"
				    	}
				    },
					data:[12,23,32,43,58]
		        },
		        {
					name:'中心关键指标',
					type:'bar',
					stack:'总量',
					label:{
						normal:{
							show:true,
							position:'insideRight'
						}
					},
					itemStyle:{
						normal:{
							color:"#f7af13"
				    	}
				    },
					data:[82,93,12,13,95]
		        },
		        {
					name:'智能柜台',
					type:'bar',
					stack:'总量',
					label:{
						normal:{
							show:true,
							position:'insideRight'
						}
					},
					itemStyle:{
						normal:{
							color:"#5fc3c8"
							
				    	}
				    },
					data:[82,93,100,100,98]
		        },
		        {
					name:'现金出纳',
					type:'bar',
					stack:'总量',
					label:{
						normal:{
							show:true,
							position:'insideRight'
						}
					},
					itemStyle:{
						normal:{
							color:"#50a6c4",
				    	}
				    },
					data:[82,90,10,13,85]
		        },
		        {
					name:'平均',
					type:'line',
					data:[220,220,220,220,220]
		        },
		      ]
	},
	pie:{
		title:{
			text:'',
			subtext:"",
			x:"center"
		},
		tooltip:{
			trigger:'item',
			formatter:"{a} <br/>{b} : {c} ({d}%)"
			},
		legend:{
			type:"scroll",
			orient:"vertical",
			right:10,
			top:20,
			bottom:20,
			data:["集中运营","智能柜台"],
			selected:[]
			},
		series:[{
			name:'指标',
			type:'pie',
			radius:"55%",
			center:["50%","50%"],
			data:[{name:"集中运营",value:6556},{name:"智能柜台",value:232}],
			itemStyle:{
				emphasis:{
					shadowBlur:10,
					shadowOffsetX:0,
					shadowColor:'rgba(0,0,0,0.5)'
				}
			}
			}]
	}
}