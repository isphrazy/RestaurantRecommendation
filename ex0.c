#include <stdio.h>
#include <limits.h>

void main(int argc, const char *argv[] ){
	int input;
	//printf("\n%s\n", argv[1]);
	if(argc == 2){
	
		//if(sscanf(argv[1], "%d", &input)){
		if(input = atoi(argv[1])){
			if(input > INT_MIN && input < INT_MAX){
				printf("%d\n", input);
			}else{
				printf("the input number is overflow, please try again\n");
			}
		}else
			printf("please input a valid number\n");
	}else{
		printf("please input one argument\n");		
	}
//char_array_to_int(argv[1]);
}

//int char_array_to_int(const char *in){
//	int index = 0;
//	char c;
//	int result
//	while((c = in[index++]) !=0 ){
//		printf("%c\n", c);		
//	}
//	return 0;
//
//}
