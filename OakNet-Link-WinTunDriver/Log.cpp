
#include "Log.h"
#include <iostream>

void logger::log(std::string msg)
{
	std::cout << "[I]: " << msg << std::endl;
}

void logger::logError(std::string error)
{
	std::cout << "[E]: " << error << std::endl;
}
